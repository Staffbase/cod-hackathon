import SwiftUI
import CoreData

class PostsApi {
    class func createPost(_ post: NewPost, completion: @escaping (_ postId: String?, _ error: Error?) -> Void) {
        if let backendUrlStr = Bundle.main.object(forInfoDictionaryKey: "BackendApiKey") as? String,
           let url = URL(string: "\(backendUrlStr)/channels/\(DataMocks.channel.id)/posts/new") {
            var request = URLRequest(url: url)
            request.addValue("application/json", forHTTPHeaderField: "Content-Type")
            request.httpMethod = "POST"
            
            let body: [String : Any] = [
                "title" : post.title,
                "message": post.message,
                "profileId": post.userId,
                "pictures": post.pictures
            ]
            
            do {
                request.httpBody = try JSONSerialization.data(withJSONObject: body, options: .sortedKeys)
            } catch let error {
                completion(nil, error)
            }
            
            let dataTask = URLSession(configuration: .default).dataTask(with: request) { data, response, error in
                guard let data = data
                else {
                    completion(nil, error)
                    return
                }
                
                do {
                    let idData = try JSONSerialization.jsonObject(with: data, options: .allowFragments)
                    let id = idData as? String
                    completion(id, nil)
                } catch {
                    completion(nil, error)
                }
            }
            dataTask.resume()
        } else {
            completion(nil, NSError(domain: "Backend URL incorrect", code: -1, userInfo: nil))
        }
    }
    
    class func editPost() {
        
    }
    
    class func commentPost() {
        
    }
    
    class func likePost(_ viewContext: NSManagedObjectContext, channelId: String, postId: String, userId: String, completion: @escaping (_ post: Post?, _ error: Error?) -> Void) {
    }
    
    class func getPosts(channel: Channel,
                        completion: @escaping (_ posts: [Post], _ error: Error?) -> Void) {
        if let backendUrlStr = Bundle.main.object(forInfoDictionaryKey: "BackendApiKey") as? String,
           let url = URL(string: "\(backendUrlStr)/channels/\(channel.id)/posts") {
            var request = URLRequest(url: url)
            request.addValue("application/json", forHTTPHeaderField: "Content-Type")
            request.httpMethod = "GET"
            
            let dataTask = URLSession(configuration: .default).dataTask(with: request) { data, response, error in
                
                guard let data = data
                else {
                    completion([], error)
                    return
                }
                
                do {
                    let postsData = try JSONSerialization.jsonObject(with: data, options: .allowFragments) as? [[String: Any]] ?? []
                    
                    completion(postsData.map {
                        Post(data: $0)
                    }, nil)
                } catch {
                    completion([], error)
                }
            }
            dataTask.resume()
        } else {
            completion([], NSError(domain: "Backend URL incorrect", code: -1, userInfo: nil))
        }
    }
}
