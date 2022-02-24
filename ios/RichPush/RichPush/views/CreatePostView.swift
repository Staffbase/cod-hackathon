import SwiftUI
import simd

@available(iOS 15.0.0, *)
struct CreatePostView: View {
    @State var title: String = ""
    @State var message: String = ""
    @State var isSheetShowing: Bool = false
    @State var isErrorPost: Bool = false
    @State var images: [String] = []
    
    var channel: Channel
    let onComplete: () -> Void
    
    var body: some View {
        NavigationView {
            Form {
                Section(header: Text("Title")) {
                    TextField("Title", text: $title)
                }
                Section(header: Text("Message")) {
                    TextEditor(text: $message)
                }
                Section(header: Text("Images")) {
                    ImageGridView($images, isShowingSheet: $isSheetShowing, isAdding: true)
                }
            }
            .navigationBarTitle("Create Post")
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                Button("Save", action: addPostAction)
            }
        }
        .sheet(isPresented: $isSheetShowing) {
            ImagePicker(isPresented: $isSheetShowing) { image in
                ImageApi.sendImage(image) { id, error in
                    guard let id = id else { return }
                    if let error = error {
                        print(error.localizedDescription)
                        return
                    }
                    
                    
                    images.append(id)
                }
            }
        }
        .alert(isPresented: $isErrorPost, content: {
            Alert(
                title: Text("Post Creation"),
                message: Text("Something went wrong.\nTry again"),
                dismissButton: .default(Text("Ok")) {
                    self.isErrorPost = false
                })
        })
        .onDisappear {
            images.removeAll()
        }
    }
    
    private func addPostAction() {
        let user = DataMocks.user
        let post: [String: Any] = [
            "title": title,
            "message": message,
            "userId": user.id,
            "pictures": images
        ]
        
        PostsApi.createPost(post) { post, error in
            guard let _ = post
            else {
                self.isErrorPost = true
                return
            }
            onComplete()
        }
    }
}

@available(iOS 15.0.0, *)
struct CreatePostView_Previews: PreviewProvider {
    static var previews: some View {
        return CreatePostView(channel: DataMocks.channel) { }
    }
}
