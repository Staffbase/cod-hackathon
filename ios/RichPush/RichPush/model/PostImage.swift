//
//  Image.swift
//  RichPush
//
//  Created by Cornelius Behrend on 24.02.22.
//

import Foundation

struct PostImage: Codable {
    var id: String
    var content: Data
    var name: String
    var type: String
}
