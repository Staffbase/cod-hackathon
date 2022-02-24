//
//  ContentView.swift
//  RichPush
//
//  Created by Cornelius Behrend on 24.02.22.
//

import SwiftUI

struct ContentView: View {
    var body: some View {
        PostsOverview(user: .constant(DataMocks.user), posts: [])
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
