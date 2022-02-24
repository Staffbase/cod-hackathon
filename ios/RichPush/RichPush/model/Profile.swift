//
//  Profile.swift
//  RichPush
//
//  Created by Cornelius Behrend on 24.02.22.
//

import Foundation

struct Profile: Codable {
    let id: String
    let picture: String
    let name: String
    let email: String
    let phone: String
    
    init(data: [String: Any]) {
        id = data["id"] as? String ?? ""
        picture = data["picture"] as? String ?? ""
        name = data["name"] as? String ?? ""
        email = data["email"] as? String ?? ""
        phone = data["phone"] as? String ?? ""
    }
}
