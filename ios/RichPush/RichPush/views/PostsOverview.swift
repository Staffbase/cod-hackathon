import SwiftUI

@available(iOS 15.0, *)
struct PostsOverview: View {
    @State var isCreateChannelPresented: Bool = false
    @State var isCreatePostPresented: Bool = false
    @State var channel: Channel? = DataMocks.channel
    
    @Binding var user: User
    
    @State var posts: [Post]
    
    var body: some View {
        NavigationView {
            List {
                Button(action: {
                    isCreatePostPresented = true
                }) {
                    PostCreationView(user: $user)
                }
                .listRowSeparator(.hidden)
                .listRowBackground(Color.clear)
                
                ForEach(posts.sorted(by: { lhs, rhs in
                    return lhs.lastUpdated.compare(rhs.lastUpdated) == .orderedDescending
                }), id: \.id) { post in
                    PostOverview(post, user: $user, channel: $channel)
                        .listRowSeparator(.hidden)
                        .listRowBackground(Color.clear)
                }
            }
            .onAppear {
                updatePosts()
            }
            .refreshable {
                updatePosts()
            }
            .listStyle(PlainListStyle())
            .sheet(isPresented: $isCreatePostPresented) {
                CreatePostView(channel: channel!) {
                    self.isCreatePostPresented = false
                    updatePosts()
                }
            }
            .navigationTitle(channel!.title)
        }
    }
    
    private func updatePosts() {
        PostsApi.getPosts(channel: channel!) { postsList, error in
            if let error = error {
                print(error.localizedDescription)
                return
            }
            
            posts = postsList
        }
    }
}


/*@available(iOS 15.0, *)
struct PostsOverview_Previews: PreviewProvider {
    static var previews: some View {
        PostsOverview(user: .constant(DataMocks.user), fetchRequest: FetchRequest())
    }
}*/
