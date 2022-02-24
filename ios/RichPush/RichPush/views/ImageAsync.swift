import SwiftUI

struct ImageAsync<Placeholder: View>: View {
    @State var imageId: String
    private let placeholder: Placeholder
    private let image: (UIImage) -> Image
    @State private var showImage: UIImage!
    
    init(
        imageId: String,
        @ViewBuilder placeholder: () -> Placeholder,
        @ViewBuilder image: @escaping (UIImage) -> Image = Image.init(uiImage:)
    ) {
        self._imageId = State(wrappedValue: imageId)
        self.placeholder = placeholder()
        self.image = image
    }
    
    var body: some View {
        Group {
            if showImage != nil {
                image(showImage)
            } else {
                placeholder
            }
        }
        .onAppear {
            fetchImage()
        }
    }
    
    func fetchImage() {
        
        ImageApi.getImageById(imageId: imageId) { image, error in
            DispatchQueue.main.async {
                if let _ = error {
                    showImage = UIImage(named: "NoImage")
                    return
                }
                
                if let image = image {
                    showImage = image
                }
            }
        }
        
    }
}
