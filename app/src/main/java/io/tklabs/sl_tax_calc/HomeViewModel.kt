package io.tklabs.sl_tax_calc

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class HomeViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    val givenSalary: StateFlow<String> =
        savedStateHandle
            .getStateFlow<String>(GIVEN_SALARY_AMOUNT, initialValue = "")

    val shouldShowDetails = MutableStateFlow(false)

    private val _grossSalary = MutableStateFlow(0.0)
    val grossSalary: Flow<Double> get() = _grossSalary

    private val _calculatedTaxAmount = MutableStateFlow(0.0)
    val calculatedTaxAmount: Flow<Double> get() = _calculatedTaxAmount

    private val _calculatedBalanceAmount = MutableStateFlow(0.0)
    val calculatedBalanceAmount: Flow<Double> get() = _calculatedBalanceAmount

    private val _calculatedTaxInfo = MutableStateFlow(listOf<TaxGroups>())
    val calculatedTaxInfo: Flow<List<TaxGroups>> get() = _calculatedTaxInfo

    init {
        viewModelScope.launch(Dispatchers.IO) {
            givenSalary.debounce(500).collectLatest {
                calculateTax(it)
            }
        }
    }

    fun onTaxChange(mGrossSalaryStr: String) {
        savedStateHandle[GIVEN_SALARY_AMOUNT] = mGrossSalaryStr
        // calculateTax()
    }

    fun onClearSalary() {
        savedStateHandle[GIVEN_SALARY_AMOUNT] = 0.0
    }
    fun calculateTax(salaryText: String) {
        val mGrossSalary = salaryText.toDoubleOrNull()
        // val mGrossSalary = givenSalaryAmount.value.toDoubleOrNull()
        _grossSalary.value = mGrossSalary ?: 0.0
        mGrossSalary?.takeIf { it > 0 }?.also { grossSalary ->
            viewModelScope.launch(Dispatchers.IO) {
                val salaryAmount: Double = grossSalary
                val annualProjection = salaryAmount * 12
                var remainingPortion: Double = annualProjection
                val taxInfoGroups = mutableListOf<TaxGroups>()
                groupRates.forEachIndexed { index, groupRate ->
                    println("xoxo: forEach ${groupRate.first} ${groupRate.second} remainingPortion:$remainingPortion")

                    if (remainingPortion > 0) {
                        val taxableBalanceAfterGroup = remainingPortion - groupRate.first
                        val taxableAmountInGroup = if (taxableBalanceAfterGroup > 0) {
                            groupRate.first
                        } else {
                            remainingPortion
                        }
                        val tax = taxableAmountInGroup / 100 * groupRate.second / 12
                        val groupInfo = TaxGroups(
                            groupIndex = index + 1,
                            taxAmount = tax,
                            rate = groupRate.second,
                            contribution = taxableAmountInGroup / 12,
                            limit = groupRate.first,
                        )
                        taxInfoGroups.add(groupInfo)
                        remainingPortion = taxableBalanceAfterGroup
                        println("xoxo: group taxableAmountInGroup:$taxableAmountInGroup remainingPortion:$remainingPortion tax$tax")
                    }
                }
                _calculatedTaxInfo.value = taxInfoGroups
                val taxAmount = taxInfoGroups
                    .takeIf { it.isNotEmpty() }
                    ?.map { it.taxAmount }
                    ?.reduce { acc, d -> acc + d }
                _calculatedTaxAmount.value = taxAmount ?: 0.0
                _calculatedBalanceAmount.value = grossSalary - (taxAmount ?: 0.0)
                shouldShowDetails.value = true
            }
        } ?: run {
            _calculatedTaxAmount.value = 0.0
            _calculatedBalanceAmount.value = 0.0
            _calculatedTaxInfo.value = listOf()
            shouldShowDetails.value = false
        }
    }

    companion object {
        const val GIVEN_SALARY_AMOUNT = "givenSalaryAmount"
        val groupRates: List<Pair<Double, Double>> = listOf(
            1200000.0 to 0.0,
            500000.0 to 6.0,
            500000.0 to 12.0,
            500000.0 to 18.0,
            500000.0 to 24.0,
            500000.0 to 30.0,
            999999999999999.0 to 36.0,
        )
    }

    data class TaxGroups(
        val groupIndex: Int,
        val rate: Double,
        val taxAmount: Double,
        val contribution: Double,
        val limit: Double,
    )
}
