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
    var shares: Int32
    var userId: String
    var profile: Profil
    var likes: [Profil]
    var comments: [Comment]
    var lastUpdated: String?
    var imagesIds: [String]
}
