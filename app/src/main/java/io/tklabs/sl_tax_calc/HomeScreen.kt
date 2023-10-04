package io.tklabs.sl_tax_calc

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import io.tklabs.sl_tax_calc.ui.theme.MyApplicationTheme
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(modifier: Modifier = Modifier, homeViewModel: HomeViewModel) {


    val givenTaxAmount = homeViewModel.givenSalaryAmount.collectAsState()
    val calculatedTaxAmount by homeViewModel.calculatedTaxAmount.collectAsState(initial = 0.0)
    val calculatedTaxInfo by homeViewModel.calculatedTaxInfo.collectAsState(initial = listOf())
    val formattedTax = String.format("%.2f",calculatedTaxAmount)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier.fillMaxSize()
    ) {

        OutlinedTextField(
            value = givenTaxAmount.value,
            onValueChange = { homeViewModel.onTaxChange(it) },
            label = { Text("Enter your salary.") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            //placeholder = { Text(text = "Enter ") },
        )
        Spacer(modifier = Modifier.height(40.dp))
        Button(onClick = { homeViewModel.calculateTax() }) {
            Text("Generate My Tax")
        }
        Text(text = "Count: $formattedTax")
        LazyColumn(
            reverseLayout = false,
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(calculatedTaxInfo) { taxInfo ->
                TaxGroupInfoView(taxGroupInfo = taxInfo)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaxGroupInfoView( modifier: Modifier = Modifier, taxGroupInfo: HomeViewModel.TaxGroups) {
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
                text = "Rate: ${String.format("%.2f",taxGroupInfo.rate)}%",
                modifier = Modifier.padding(all = 4.dp),
                style = MaterialTheme.typography.titleSmall
            )
            Text(
                text = "Your contribution: ${String.format("%.2f",taxGroupInfo.contribution)}",
                modifier = Modifier.padding(all = 4.dp),
                style = MaterialTheme.typography.titleSmall
            )
            Text(
                text = "Tax: ${String.format("%.2f",taxGroupInfo.taxAmount)      }",
                modifier = Modifier.padding(all = 4.dp),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}
@Preview(showBackground = true)
@Composable
fun HomePreview() {
    val item = HomeViewModel.TaxGroups(groupIndex = 1, taxAmount = 100.0, rate = 15.0, contribution = 200.0, limit = 50000.0)
    MyApplicationTheme {
        TaxGroupInfoView(taxGroupInfo = item)
    }
}
