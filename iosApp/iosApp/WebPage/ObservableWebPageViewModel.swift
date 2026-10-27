import Foundation
import Observation
import Shared

@MainActor
@Observable
final class ObservableWebPageViewModel {
    private(set) var state: WebPageViewState

    @ObservationIgnored private let owner: WebPageViewModelOwner

    init(owner: WebPageViewModelOwner = WebPageViewModelOwner()) {
        self.owner = owner
        self.state = owner.currentState
        owner.observe { [weak self] state in
            MainActor.assumeIsolated {
                self?.state = state
            }
        }
    }

    deinit {
        owner.clear()
    }

    var resultText: String {
        state.text.data as String? ?? ""
    }

    var errorMessage: String? {
        guard let error = state.text.error else { return nil }
        return error.message ?? String(localized: "Something went wrong")
    }

    func onUrlChanged(_ url: String) {
        owner.viewModel.onUrlChanged(url: url)
    }

    func onSelectorChanged(_ selector: String) {
        owner.viewModel.onSelectorChanged(selector: selector)
    }

    func onPresetSelected(_ preset: SelectorPreset) {
        owner.viewModel.onPresetSelected(preset: preset)
    }

    func extract() {
        owner.viewModel.extract()
    }
}
