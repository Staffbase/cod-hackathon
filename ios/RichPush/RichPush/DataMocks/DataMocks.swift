//
//  MockUser.swift
//  RichPush
//
//  Created by Cornelius Behrend on 24.02.22.
//

import Foundation

struct ProfilMock {
    static let profil = Profile(data: [
        "id": "kSReChubXn43Sv1",
        "picture": "iiIvdF4Q6Ng2MYk",
        "name": "Bart Simpson",
        "email": "bart@staffbase.com",
        "phone": "123-123-123"
    ])
}

struct DataMocks {
    static let user = User(
        id: "SaNMVO1j7f5YTCO",
        profile: ProfilMock.profil
    
    )
    
    static let channel = Channel(
        id: "zPZ8kWimDxHLZkY",
        title: "CoD Channel",
        description: "Hacktahon"
    )

}

