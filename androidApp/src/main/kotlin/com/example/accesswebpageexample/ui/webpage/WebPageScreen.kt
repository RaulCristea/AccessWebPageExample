package com.example.accesswebpageexample.ui.webpage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.accesswebpageexample.R
import com.example.accesswebpageexample.core.DataState
import com.example.accesswebpageexample.di.SharedModule
import com.example.accesswebpageexample.presentation.webpage.model.SelectorPreset
import com.example.accesswebpageexample.presentation.webpage.state.WebPageViewState
import com.example.accesswebpageexample.presentation.webpage.viewmodel.WebPageViewModel
import com.example.accesswebpageexample.ui.theme.AppTheme

@Composable
fun WebPageScreen(
    viewModel: WebPageViewModel = viewModel(factory = SharedModule.webPageViewModelFactory),
) {
    val state: WebPageViewState by viewModel.state.collectAsStateWithLifecycle()

    WebPageContent(
        state = state,
        onUrlChanged = viewModel::onUrlChanged,
        onSelectorChanged = viewModel::onSelectorChanged,
        onPresetSelected = viewModel::onPresetSelected,
        onExtract = viewModel::extract,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WebPageContent(
    state: WebPageViewState,
    onUrlChanged: (String) -> Unit,
    onSelectorChanged: (String) -> Unit,
    onPresetSelected: (SelectorPreset) -> Unit,
    onExtract: () -> Unit,
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text(text = stringResource(R.string.web_page_title)) }) },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            OutlinedTextField(
                value = state.url,
                onValueChange = onUrlChanged,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = stringResource(R.string.web_page_url_label)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri, imeAction = ImeAction.Next),
            )
            SelectorField(
                selector = state.selector,
                presets = state.presets,
                onSelectorChanged = onSelectorChanged,
                onPresetSelected = onPresetSelected,
                onExtract = onExtract,
            )
            ExtractButton(
                isLoading = state.isLoading,
                enabled = state.canExtract,
                onClick = onExtract,
            )
            ResultField(text = state.text)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SelectorField(
    selector: String,
    presets: List<SelectorPreset>,
    onSelectorChanged: (String) -> Unit,
    onPresetSelected: (SelectorPreset) -> Unit,
    onExtract: () -> Unit,
) {
    var expanded: Boolean by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { isExpanded -> expanded = isExpanded },
    ) {
        OutlinedTextField(
            value = selector,
            onValueChange = onSelectorChanged,
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable),
            label = { Text(text = stringResource(R.string.web_page_selector_label)) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded,
                    modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.SecondaryEditable),
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Ascii, imeAction = ImeAction.Go),
            keyboardActions = KeyboardActions(onGo = { onExtract() }),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            presets.forEach { preset ->
                DropdownMenuItem(
                    text = { Text(text = stringResource(preset.labelRes)) },
                    trailingIcon = { Text(text = preset.selector, color = MaterialTheme.colorScheme.onSurfaceVariant) },
                    onClick = {
                        expanded = false
                        onPresetSelected(preset)
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}

@Composable
private fun ExtractButton(
    isLoading: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        enabled = enabled,
    ) {
        Box(contentAlignment = Alignment.Center) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(18.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            } else {
                Text(text = stringResource(R.string.web_page_extract))
            }
        }
    }
}

@Composable
private fun ResultField(text: DataState<String>) {
    val errorMessage: String? = text.error?.let { error ->
        error.message ?: stringResource(R.string.web_page_unknown_error)
    }

    OutlinedTextField(
        value = text.data.orEmpty(),
        onValueChange = {},
        modifier = Modifier.fillMaxWidth(),
        readOnly = true,
        label = { Text(text = stringResource(R.string.web_page_result_label)) },
        placeholder = { Text(text = stringResource(R.string.web_page_result_placeholder)) },
        isError = errorMessage != null,
        supportingText = errorMessage?.let { message -> { Text(text = message) } },
        minLines = 3,
    )
}

@Preview(showBackground = true)
@Composable
private fun WebPageContentPreview() {
    AppTheme {
        WebPageContent(
            state = WebPageViewState.initial().copy(text = DataState.Value("Google")),
            onUrlChanged = {},
            onSelectorChanged = {},
            onPresetSelected = {},
            onExtract = {},
        )
    }
}
