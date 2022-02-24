import Foundation
import PhotosUI
import SwiftUI

struct ImagePicker: UIViewControllerRepresentable {
    @Binding var isPresented: Bool // close the modal view
    
    let onComplete: (UIImage) -> Void
    
    func makeUIViewController(context: Context) -> some UIViewController {
        var configuration = PHPickerConfiguration(photoLibrary: PHPhotoLibrary.shared())
        configuration.filter = .images // filter only to images
        configuration.selectionLimit = 0 // ignore limit
        
        let photoPickerViewController = PHPickerViewController(configuration: configuration)
        photoPickerViewController.delegate = context.coordinator // Use Coordinator for delegation
        return photoPickerViewController
    }
    
    func updateUIViewController(_ uiViewController: UIViewControllerType, context: Context) { }
    
    func makeCoordinator() -> Coordinator {
        Coordinator(self)
    }
    
    // Create the Coordinator, in this case it is a way to communicate with the PHPickerViewController
    class Coordinator: PHPickerViewControllerDelegate {
        private var parent: ImagePicker
        
        init(_ parent: ImagePicker) {
            self.parent = parent
        }
        
        func picker(_ picker: PHPickerViewController, didFinishPicking results: [PHPickerResult]) {
            // unpack the selected items
            for image in results {
                
                if !image.itemProvider.canLoadObject(ofClass: UIImage.self) {
                    print("Can't load asset")
                    continue
                }
                
                image.itemProvider.loadObject(ofClass: UIImage.self) { [weak self] newImage, error in
                    if let image = newImage as? UIImage {
                        // Add new image and pass it back to the main view
                        self?.parent.onComplete(image)
                    }
                    
                    if let error = error {
                        print("Can't load image \(error.localizedDescription)")
                        return
                    }
                }
            }
            
            // close the modal view
            parent.isPresented = false
        }
    }
}
