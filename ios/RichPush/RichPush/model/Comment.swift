//
//  Comment.swift
//  RichPush
//
//  Created by Cornelius Behrend on 24.02.22.
//

import Foundation

struct Comment: Codable {
    let id: String
    let profile: Profile
    let message: String
    let sent: String
}
