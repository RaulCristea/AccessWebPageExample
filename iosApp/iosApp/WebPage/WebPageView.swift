import SwiftUI

struct WebPageView: View {
    @State private var viewModel = ObservableWebPageViewModel()

    var body: some View {
        NavigationStack {
            Form {
                sourceSection
                extractSection
                resultSection
            }
            .navigationTitle("Web Page Text")
        }
    }

    private var sourceSection: some View {
        Section("Source") {
            TextField("URL", text: urlBinding)
                .keyboardType(.URL)
                .textContentType(.URL)
                .textInputAutocapitalization(.never)
                .autocorrectionDisabled()
                .submitLabel(.next)
            HStack {
                TextField("CSS selector", text: selectorBinding)
                    .textInputAutocapitalization(.never)
                    .autocorrectionDisabled()
                    .submitLabel(.go)
                    .onSubmit(viewModel.extract)
                presetMenu
            }
        }
    }

    private var presetMenu: some View {
        Menu {
            ForEach(viewModel.state.presets, id: \.self) { preset in
                Button {
                    viewModel.onPresetSelected(preset)
                } label: {
                    if preset == viewModel.state.selectedPreset {
                        Label(preset.label, systemImage: "checkmark")
                    } else {
                        Text(preset.label)
                    }
                    Text(preset.selector)
                }
            }
        } label: {
            Image(systemName: "list.bullet")
        }
        .accessibilityLabel("Selector presets")
    }

    private var extractSection: some View {
        Section {
            Button(action: viewModel.extract) {
                HStack {
                    Spacer()
                    if viewModel.state.isLoading {
                        ProgressView()
                    } else {
                        Text("Extract")
                    }
                    Spacer()
                }
            }
            .disabled(!viewModel.state.canExtract)
        }
    }

    private var resultSection: some View {
        Section {
            TextField("Nothing extracted yet", text: .constant(viewModel.resultText), axis: .vertical)
                .lineLimit(3...12)
                .disabled(true)
                .foregroundStyle(.primary)
        } header: {
            Text("Extracted text")
        } footer: {
            if let errorMessage = viewModel.errorMessage {
                Text(errorMessage)
                    .foregroundStyle(.red)
            }
        }
    }

    private var urlBinding: Binding<String> {
        Binding(
            get: { viewModel.state.url },
            set: viewModel.onUrlChanged
        )
    }

    private var selectorBinding: Binding<String> {
        Binding(
            get: { viewModel.state.selector },
            set: viewModel.onSelectorChanged
        )
    }
}

#Preview {
    WebPageView()
}
