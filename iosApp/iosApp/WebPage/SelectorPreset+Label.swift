import SwiftUI
import Shared

extension SelectorPreset {
    var label: LocalizedStringKey {
        switch self {
        case .title: "Page title"
        case .headings: "Main headings"
        case .subheadings: "Subheadings"
        case .paragraphs: "Paragraphs"
        case .links: "Links"
        case .wholePage: "Whole page"
        default: LocalizedStringKey(selector)
        }
    }
}
