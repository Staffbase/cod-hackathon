import SwiftUI

struct PostCreationView: View {
    @Binding var user: User
    
    var body: some View {
        HStack(alignment: .top) {
            ImageAsync(
                imageId: user.profile.picture,
                placeholder: {
                    ZStack {
                        ProgressView()
                    }
                },
                image: {
                    Image(uiImage: $0)
                        .resizable()
                })
                .scaledToFill()
                .frame(width: 80, height: 80)
                .cornerRadius(40.0)
            
            Spacer()
            
            VStack(alignment: .center, spacing: 5.0) {
                Spacer()
                Text("What's on your mind?")
                    .font(.subheadline)
                    .foregroundColor(Color.gray)
                    .multilineTextAlignment(.leading)
                    .lineLimit(1)
                Spacer()
            }
            Spacer()
        }
        .padding()
        .background(Color.white)
        .cornerRadius(10)
        .shadow(radius: 5)
        .overlay(
            RoundedRectangle(cornerRadius: 10)
                .stroke(Color(.sRGB, red: 150/255, green: 150/255, blue: 150/255, opacity: 0.1), lineWidth: 1)
        )
        .frame(height: 100)
        .padding([.top, .bottom])
    }
}

struct PostCreationView_Previews: PreviewProvider {
    static var previews: some View {
        PostCreationView(user: .constant(DataMocks.user))
    }
}
