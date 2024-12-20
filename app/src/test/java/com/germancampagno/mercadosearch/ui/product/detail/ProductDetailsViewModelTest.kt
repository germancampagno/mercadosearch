import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.germancampagno.mercadosearch.R
import com.germancampagno.mercadosearch.data.model.ErrorMessage
import com.germancampagno.mercadosearch.data.model.Product
import com.germancampagno.mercadosearch.data.model.ProductDescriptionResponse
import com.germancampagno.mercadosearch.data.repository.ProductRepository
import com.germancampagno.mercadosearch.ui.product.detail.ProductDetailsViewModel
import com.germancampagno.mercadosearch.ui.util.ProductFormatter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ProductDetailsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Mock
    private lateinit var repository: ProductRepository

    private lateinit var viewModel: ProductDetailsViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testScope.cancel()
    }

    @Test
    fun `fetchProductDetails should update uiState with product details on success`() = testScope.runTest {
        val productId = "testProductId"
        val savedStateHandle = SavedStateHandle(mapOf("productId" to productId))
        viewModel = ProductDetailsViewModel(repository, savedStateHandle)

        val productDetails = Product(id = productId, title = "Test Product", price = 100.0, currency = "USD", thumbnail = null, description = null, permalink = null, condition = "new", pictures = null, address = null, sellerAddress = null, formattedPrice = null, formattedAddress = null)
        val productDescription = ProductDescriptionResponse("Description 1")
        val formattedCurrency = "US$ 100"

        `when`(repository.getProductDetails(productId)).thenReturn(productDetails)
        `when`(repository.getProductDescription(productId)).thenReturn(productDescription)

        viewModel.fetchProductDetails(productId)
        advanceUntilIdle()

        val uiState = viewModel.uiState.value
        val product = uiState.productDetails
        Assert.assertEquals(product!!.id, productId)
        Assert.assertEquals(product.title, "Test Product")
        Assert.assertEquals(product.formattedPrice, formattedCurrency)
        Assert.assertFalse(uiState.isLoading)
        Assert.assertNull(uiState.errorMessage)
    }

    @Test
    fun `fetchProductDetails should update uiState with error message on failure`() = testScope.runTest {
        val productId = "testProductId"
        val savedStateHandle = SavedStateHandle(mapOf("productId" to productId))
        viewModel = ProductDetailsViewModel(repository, savedStateHandle)

        val exceptionMessage = "Network error"
        `when`(repository.getProductDetails(productId)).thenThrow(RuntimeException(exceptionMessage))

        viewModel.fetchProductDetails(productId)
        advanceUntilIdle()

        val uiState = viewModel.uiState.value
        Assert.assertNull(uiState.productDetails)
        Assert.assertFalse(uiState.isLoading)
        Assert.assertEquals(ErrorMessage.Exception(exceptionMessage), uiState.errorMessage)
    }

    @Test
    fun `fetchProductDetails should update uiState with error message when productId is null`() = testScope.runTest {
        val savedStateHandle = SavedStateHandle()
        viewModel = ProductDetailsViewModel(repository, savedStateHandle)

        viewModel.fetchProductDetails(null)
        advanceUntilIdle()

        val uiState = viewModel.uiState.value
        Assert.assertNull(uiState.productDetails)
        Assert.assertFalse(uiState.isLoading)
        Assert.assertEquals(ErrorMessage.InvalidId, uiState.errorMessage)
    }

    @Test
    fun `getConditionResId should return correct resource id`() {
        val savedStateHandle = SavedStateHandle()
        viewModel = ProductDetailsViewModel(repository, savedStateHandle)

        val newConditionResId = viewModel.getConditionResId(Product.Condition.New)
        val usedConditionResId = viewModel.getConditionResId(Product.Condition.Used)

        Assert.assertEquals(R.string.product_condition_new, newConditionResId)
        Assert.assertEquals(R.string.product_condition_used, usedConditionResId)
    }
}
