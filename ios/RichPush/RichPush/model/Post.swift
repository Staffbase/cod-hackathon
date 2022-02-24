//
//  Post.swift
//  RichPush
//
//  Created by Cornelius Behrend on 24.02.22.
//

import Foundation

struct NewPost: Codable {
    let title: String
    let message: String
    let userId: String
    let pictures: [String]
}

struct Post: Codable {
    var id: String
    var title: String
    var message: String
    var shares: Int
    var userId: String
    var profile: Profile
    var lastUpdated: Date
    var pictures: [String]
    var comments: [Comment] = []
    var likes: [Profile] = []
    
    init(data: [String: Any]) {
        id = data["id"] as? String ?? ""
        title = data["title"] as? String ?? ""
        message = data["message"] as? String ?? ""
        shares = data["shares"] as? Int ?? 0
        userId = data["userId"] as? String ?? ""
        
        pictures = data["pictures"] as? [String] ?? []
        profile = Profile(data: data["profile"] as? [String: Any] ?? [:])
        
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS"
        lastUpdated = dateFormatter.date(from: data["lastUpdated"] as? String ?? "") ?? Date()
    }
}
