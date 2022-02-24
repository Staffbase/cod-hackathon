import SwiftUI
import PhotosUI

struct ImageGridView: View {
    @Binding var isShowingSheet: Bool
    @State var isNotPermitted: Bool = false
    @Binding var images: [String]
    var isAdding: Bool
    
    init(_ images: Binding<[String]>, isShowingSheet: Binding<Bool>, isAdding: Bool = false) {
        self._images = images
        self._isShowingSheet = isShowingSheet
        self.isAdding = isAdding
    }
    
    var body: some View {
        ScrollView(.horizontal, showsIndicators: false) {
            ScrollViewReader { sp in
                LazyHGrid(
                    rows: [
                        .init(.adaptive(minimum: 100, maximum: 120))
                    ]
                ) {
                    ForEach(images, id: \.self) { image in
                        ImageAsync(
                            imageId: image,
                            placeholder: {
                                ZStack {
                                    ProgressView()
                                }
                            },
                            image: {
                                Image(uiImage: $0)
                                    .resizable()
                            }
                        )
                        .scaledToFill()
                        .frame(width: 100, height: 100)
                        .cornerRadius(3.0)
                    }
                    
                    if isAdding {
                        Button(action: {
                            let authStatus = PHPhotoLibrary.authorizationStatus()
                            if authStatus == .restricted || authStatus == .denied {
                                self.isNotPermitted = true
                                return
                            }
                            
                            // we either already have permission or can prompt
                            if authStatus == .authorized {
                                self.isShowingSheet = true
                            } else {
                                PHPhotoLibrary.requestAuthorization(for: .readWrite) { status in
                                    if status == PHAuthorizationStatus.authorized {
                                        self.isShowingSheet = true
                                    } else {
                                        self.isNotPermitted = true
                                    }
                                }
                            }
                        }, label: {
                            Label("", systemImage: "plus")
                        })
                        .padding()
                        .frame(width: 100, height: 100)
                        .foregroundColor(.gray)
                        .background(Color.white)
                        .cornerRadius(3.0)
                        .shadow(color: .gray, radius: 2)
                    }
                }
                .padding([.leading], 3)
                .padding([.top, .bottom], 10)
            }
        }
        .alert(isPresented: $isNotPermitted, content: {
            Alert(
                title: Text("Library access permission denied"),
                message: Text("Without this permission the app cannot access your photos"),
                primaryButton: .default(Text("Change Permissions")) {
                    let url = URL(string:UIApplication.openSettingsURLString)
                    if UIApplication.shared.canOpenURL(url!){
                        // can open succeeded.. opening the url
                        UIApplication.shared.open(url!, options: [:], completionHandler: nil)
                    }
                },
                secondaryButton: .default(Text("Ok")) {
                    self.isNotPermitted = false
                })
        })
    }
}

struct ImageGridView_Previews: PreviewProvider {
    static var previews: some View {
        ImageGridView(.constant([]), isShowingSheet: .constant(false), isAdding: true)
    }
}
