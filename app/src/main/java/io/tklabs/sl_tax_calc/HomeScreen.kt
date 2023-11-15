package io.tklabs.sl_tax_calc

import android.icu.text.NumberFormat
import android.icu.util.Currency
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import io.tklabs.sl_tax_calc.ui.theme.MyApplicationTheme
import io.tklabs.sl_tax_calc.ui.theme.YelloBg2
import io.tklabs.sl_tax_calc.ui.theme.customFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel,
    isPreview: Boolean = false
) {
    val showDetails by homeViewModel.shouldShowDetails.collectAsState(initial = false)
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(vertical = 0.dp, horizontal = 0.dp)
            .background(MaterialTheme.colorScheme.primaryContainer),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        TopContainer(homeViewModel = homeViewModel)
        if (showDetails || isPreview) {
            TaxInfoContainer(homeViewModel = homeViewModel)
            TaxDetailsContainer(homeViewModel = homeViewModel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopContainer(modifier: Modifier = Modifier, homeViewModel: HomeViewModel) {
    val givenSalary = homeViewModel.givenSalary.collectAsState(initial = "")
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(vertical = 0.dp, horizontal = 0.dp)
            //.background(MaterialTheme.colorScheme.primary)
            .fillMaxWidth()
    ) {
        ElevatedCard(
            shape = MaterialTheme.shapes.extraLarge,
            modifier = modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .fillMaxWidth()
                .padding(vertical = 15.dp, horizontal = 15.dp),
            //elcevation = 3.dp
        ) {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.inversePrimary)
                    .padding(top = 0.dp, bottom = 15.dp, start = 15.dp, end = 15.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Tax Calculator Sri Lanka",
                    color = MaterialTheme.colorScheme.onPrimary,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontFamily = customFontFamily,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier
                        .padding(vertical = 15.dp, horizontal = 0.dp)
                        .fillMaxWidth()
                )
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = givenSalary.value,
                    singleLine = true,
                    shape = RoundedCornerShape(20.dp),
                    onValueChange = { homeViewModel.onTaxChange(it) },
                    label = { Text("Enter your monthly salary.") },
                    leadingIcon = { Text("LKR") },
                    trailingIcon = {
                        Row(
                            modifier = Modifier
                                .padding(end = 4.dp)
                                .size(30.dp)
                                .clickable {
                                    homeViewModel.onClearSalary()
                                }
                                .border(1.dp, MaterialTheme.colorScheme.onBackground, CircleShape)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_clear),
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(all = 4.dp)
                                    .fillMaxSize()
                            )
                        }

                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    placeholder = { Text("100000.00") }
                )
            }
        }
    }
}


@Composable
fun TextLabel(text: String) {
    Text(
        text = text,
        color = MaterialTheme.colorScheme.onPrimaryContainer,
        textAlign = TextAlign.Start,
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier
            .padding(vertical = 0.dp, horizontal = 0.dp)
            .wrapContentWidth(align = Alignment.CenterHorizontally)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextChip(text: String, onClick: () -> Unit) {
    ElevatedAssistChip(
        shape = RoundedCornerShape(20.dp),
        onClick = onClick,
        label = {
            Text(
                text = text,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .padding(vertical = 2.dp, horizontal = 3.dp)
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaxInfoContainer(modifier: Modifier = Modifier, homeViewModel: HomeViewModel) {
    val salaryAmount by homeViewModel.grossSalary.collectAsState(initial = 0.0)
    val taxAmount by homeViewModel.calculatedTaxAmount.collectAsState(initial = 0.0)
    val remainingAmount by homeViewModel.calculatedBalanceAmount.collectAsState(initial = 0.0)

    /*  val formattedTax = String.format("%.2f", taxAmount)
      val formattedBalance = String.format("%.2f", remainingAmount)
      val formattedSalary = String.format("%.2f", salaryAmount)*/
    val formatter = NumberFormat.getCurrencyInstance()
    formatter.currency = Currency.getInstance("LKR")
    val formattedTax = formatter.format(taxAmount)
    val formattedBalance = formatter.format(remainingAmount)
    val formattedSalary = formatter.format(salaryAmount)

    ElevatedCard(
        shape = MaterialTheme.shapes.extraLarge,
        modifier = modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(top = 10.dp, bottom = 10.dp, start = 15.dp, end = 15.dp)
            .fillMaxWidth()

        //elcevation = 3.dp
    ) {
        Row(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.tertiaryContainer)
                .padding(top = 10.dp, bottom = 10.dp, start = 10.dp, end = 10.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
        ) {
            Column(
                modifier = Modifier
                    .weight(6f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                PieChart(
                    data = mapOf(
                        Pair("Sample-1", 150),
                        Pair("Sample-2", 120),
                        Pair("Sample-3", 110),
                        Pair("Sample-4", 170),
                        Pair("Sample-5", 120),
                    )
                )
            }
            Column(
                modifier = Modifier
                    .weight(4f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                TextLabel(text = "Gross Salary")
                TextChip(text = formattedSalary, onClick = {})
                Spacer(modifier = Modifier.height(5.dp))
                TextLabel(text = "Tax Deduction")
                TextChip(text = formattedTax, onClick = {})
                Spacer(modifier = Modifier.height(5.dp))
                TextLabel(text = "Remaining")
                TextChip(text = formattedBalance, onClick = {})
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaxDetailsContainer(modifier: Modifier = Modifier, homeViewModel: HomeViewModel) {
    val calculatedTaxAmount by homeViewModel.calculatedTaxAmount.collectAsState(initial = 0.0)
    val calculatedTaxInfo by homeViewModel.calculatedTaxInfo.collectAsState(initial = listOf())
    val formattedTax = String.format("%.2f", calculatedTaxAmount)
    Surface(
        shape = MaterialTheme.shapes.medium,
        modifier = modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .fillMaxSize()
            .padding(top = 0.dp, bottom = 15.dp, start = 15.dp, end = 15.dp),
        shadowElevation = 3.dp
    ) {
        Column(
            modifier = Modifier
                .background(YelloBg2)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            //Spacer(modifier = Modifier.height(40.dp))
            /*Button(onClick = { homeViewModel.calculateTax() }) {
                Text("Generate My Tax")
            }*/
            Text(
                text = "Total Tax: $formattedTax",
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .padding(vertical = 15.dp, horizontal = 15.dp)
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(15.dp))
            //Text(text = "Count: $formattedTax")
            LazyColumn(
                reverseLayout = false, modifier = Modifier.fillMaxSize()
            ) {
                items(calculatedTaxInfo) { taxInfo ->
                    TaxGroupInfoView(taxGroupInfo = taxInfo)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaxGroupInfoView(modifier: Modifier = Modifier, taxGroupInfo: HomeViewModel.TaxGroups) {
    Surface(
        color = MaterialTheme.colorScheme.primary,
        onClick = {},
        //shape = MaterialTheme.shapes.medium,
        shadowElevation = 3.dp,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "Tier ${taxGroupInfo.groupIndex}",
                modifier = Modifier.padding(all = 4.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Rate: ${String.format("%.2f", taxGroupInfo.rate)}%",
                modifier = Modifier.padding(all = 4.dp),
                style = MaterialTheme.typography.titleSmall
            )
            Text(
                text = "Your contribution: ${String.format("%.2f", taxGroupInfo.contribution)}",
                modifier = Modifier.padding(all = 4.dp),
                style = MaterialTheme.typography.titleSmall
            )
            Text(
                text = "Tax: ${String.format("%.2f", taxGroupInfo.taxAmount)}",
                modifier = Modifier.padding(all = 4.dp),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}
/*
@Composable
fun PieChartSample() {

    val testPieChartData: List<PieChartData> = listOf(
        PieChartData(
            partName = "part A",
            data = 500.0,
            color = Color(0xFF22A699),
        ),
        PieChartData(
            partName = "Part B",
            data = 700.0,
            color = Color(0xFFF2BE22),
        ),
        PieChartData(
            partName = "Part C",
            data = 500.0,
            color = Color(0xFFF29727),
        ),
        PieChartData(
            partName = "Part D",
            data = 100.0,
            color = Color(0xFFF24C3D),
        ),
    )

    PieChart(
        modifier = Modifier
            .height(200.dp)
          //  .wrapContentHeight(align = Alignment.Top)
            .        fillMaxWidth(),
        pieChartData = testPieChartData,
        ratioLineColor = Color.LightGray,
        legendPosition = LegendPosition.TOP,
        textRatioStyle = TextStyle(color = Color.Gray),
    )
}
*/
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MyApplicationTheme {
        HomeScreen(homeViewModel = HomeViewModel(SavedStateHandle()), isPreview = true)
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    val item = HomeViewModel.TaxGroups(
        groupIndex = 1, taxAmount = 100.0, rate = 15.0, contribution = 200.0, limit = 50000.0
    )
    MyApplicationTheme {
        TaxGroupInfoView(taxGroupInfo = item)
    }
}