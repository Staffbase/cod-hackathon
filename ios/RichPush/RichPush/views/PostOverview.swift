import SwiftUI

struct PostOverview: View {
    @State var post: Post
    @Binding var user: User
    @Binding var channel: Channel?
    
    init(_ post: Post, user: Binding<User>, channel: Binding<Channel?>) {
        _post = State(initialValue: post)
        _user = user
        _channel = channel
    }
    
    var body: some View {
        VStack(alignment: .leading, spacing: 8.0) {
            PostOverviewHeader(post: $post)
            PostOverviewContent(post: $post)
            PostOverviewFooter(post: $post, user: $user, channel: $channel)
        }
        .padding()
        .background(Color.white)
        .cornerRadius(10)
        .shadow(radius: 5)
        .overlay(
            RoundedRectangle(cornerRadius: 10)
                .stroke(Color(.sRGB, red: 150/255, green: 150/255, blue: 150/255, opacity: 0.1), lineWidth: 1)
        )
        .padding([.top], 2.0)
    }
}

struct PostOverviewHeader: View {
    @Binding var post: Post
    
    var body: some View {
        HStack {
            ImageAsync(
                imageId: post.profile.picture,
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
            .frame(width: 50, height: 50)
            .cornerRadius(25)
            .padding(.trailing, 10)
            
            VStack(alignment: .leading) {
                Text(post.profile.name)
                    .font(.title2)
                    .fontWeight(.semibold)
                Text(post.lastUpdated.formatted(date: .abbreviated, time: .shortened))
                    .font(.footnote)
                    .foregroundColor(Color.gray)
            }
        }
    }
}

struct PostOverviewFooter: View {
    @Environment(\.managedObjectContext) var managedObjectContext
    @Binding var post: Post
    @Binding var user: User
    @Binding var channel: Channel?
    @State var isLiked: Bool = false
    
    var body: some View {
        VStack {
            HStack {
                Image(systemName: "heart.fill")
                Text("\(post.likes.count > 1000 ? String(format: "%.1fK", post.likes.count/1000) : "\(Int(post.likes.count))")")
                Spacer()
                Text("\(post.comments.count > 1000 ? String(format: "%.1fK", post.comments.count/1000) : "\(Int(post.comments.count))") Comments")
                Text("\(post.shares > 1000 ? String(format: "%.1fK", post.shares/1000) : "\(Int(post.shares))") Shares")
            }
            
            Divider()
            
            HStack {
                Button(action: {
                }, label: {
                    HStack(spacing: 10) {
                        Image(systemName: isLiked ? "heart.fill" : "heart")
                        Text("Like")
                    }
                })
                
                Spacer()
                
                Button(action: {
                   
                }, label: {
                    HStack(spacing: 10) {
                        Image(systemName: "message")
                        Text("Comment")
                    }
                })
                
                Spacer()
                
                Button(action: {
                    
                }, label: {
                    HStack(spacing: 10) {
                        Image(systemName: "paperplane")
                        Text("Share")
                    }
                })
            }
        }
        .onAppear {
//            self.isLiked = (self.post.likes?.allObjects as? [Profile] ?? []).contains(user!.has!)
        }
    }
}

struct PostOverviewContent: View {
    @Binding var post: Post
    @State var images: [String] = []
    
    var body: some View {
        VStack(alignment: .leading) {
            Text(post.title)
                .font(.title3)
            Text(post.message)
                .font(.body)
                .lineLimit(6)
            
            if post.pictures.count > 0 {
                ImageGridView($images, isShowingSheet: .constant(false))
                    .frame(minWidth: 0, idealWidth: 100, maxWidth: .infinity, minHeight: 0, idealHeight: 100, maxHeight: 120)
            }
        }
        .onAppear {
            self.images = post.pictures
        }
    }
}


/*struct PostOverview_Previews: PreviewProvider {
    static var previews: some View {
        PostOverview(Post(), user: .constant(DataMocks.user), channel: .constant(DataMocks.channel))
    }
}*/
