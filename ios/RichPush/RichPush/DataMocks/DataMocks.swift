//
//  MockUser.swift
//  RichPush
//
//  Created by Cornelius Behrend on 24.02.22.
//

import Foundation

struct ProfilMock {
    static let profil = Profil(id: "123", picture: "123", name: "Demo User", email: "foo@bar.com", phone: "123-123-123")
}

struct DataMocks {
    static let demoProfile = Profil(id: "123", picture: "123", name: "Demo User", email: "foo@bar.com", phone: "123-123-123")
    static let user = User(id: "123", profile: ProfilMock.profil)
    static let channel = Channel(id: "123", title: "CoD Channel", description: "Hacktahon")
    static let demoPost = Post(id: "123", title: "Test", message: "Demo Message", shares: 2002, userId: "123", profile: ProfilMock.profil, likes: [], comments: [], lastUpdated: nil, imagesIds: [])
}

