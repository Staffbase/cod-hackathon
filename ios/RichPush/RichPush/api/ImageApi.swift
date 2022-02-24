import Foundation
import SwiftUI

class ImageApi {
    
    class func convertFormField(named name: String, value: String, using boundary: String) -> String {
        var fieldString = "--\(boundary)\r\n"
        fieldString += "Content-Disposition: form-data; name=\"\(name)\"\r\n"
        fieldString += "\r\n"
        fieldString += "\(value)\r\n"
        
        return fieldString
    }
    
    private class func convertFileData(fileName: String, mimeType: String, fileData: Data, using boundary: String) -> Data {
        let data = NSMutableData()
        
        data.append("--\(boundary)\r\n")
        data.append("Content-Disposition: form-data; name=\"files\"; filename=\"\(fileName)\"\r\n")
        data.append("Content-Type: \(mimeType)\r\n\r\n")
        data.append(fileData)
        data.append("\r\n")
        
        return data as Data
    }
    
    class func sendImage(_ image: UIImage, completion: @escaping (_ id: String?, _ error: Error?) -> Void) {
        if let backendUrlStr = Bundle.main.object(forInfoDictionaryKey: "BackendApiKey") as? String,
           let url = URL(string: "\(backendUrlStr)/images/new") {
            let boundary = "\(UUID().uuidString)"
            var request = URLRequest(url: url)
            request.setValue("multipart/form-data; boundary=Boundary-\(boundary)", forHTTPHeaderField: "Content-Type")
            request.httpMethod = "POST"
            
            let httpBody = NSMutableData()
            
            httpBody.append(
                convertFileData(
                    fileName: "image.jpeg",
                    mimeType: "img/jpeg",
                    fileData: image.jpegData(compressionQuality: 1.0)!,
                    using: "Boundary-\(boundary)")
            )
            
            httpBody.append("--Boundary-\(boundary)--")
            
            request.httpBody = httpBody as Data
            
            let dataTask = URLSession(configuration: .default).dataTask(with: request) { data, response, error in
                if let error = error {
                    completion(nil, error)
                    return
                }
                
                if let data = data {
                    do {
                        let idData = try JSONSerialization.jsonObject(with: data, options: .allowFragments)
                        let id = idData as? String
                        completion(id, nil)
                    } catch {
                        completion(nil, error)
                    }
                }
            }
            dataTask.resume()
        } else {
            completion(nil, NSError(domain: "Backend URL incorrect", code: -1, userInfo: nil))
        }
    }
    
    class func getImageById(imageId: String, completion: @escaping (_ image: UIImage?, _ error: Error?) -> Void) {
        if let backendUrlStr = Bundle.main.object(forInfoDictionaryKey: "BackendApiKey") as? String, let url = URL(string: "\(backendUrlStr)/images/\(imageId)") {
            
            var request = URLRequest(url: url)
            request.httpMethod = "GET"
            
            let dataTask = URLSession(configuration: .default).dataTask(with: request) { data, response, error in
                if let error = error {
                    completion(nil, error)
                    return
                }
                
                if let data = data {
                    completion(UIImage(data: data), nil)
                    return
                }
            }
            
            dataTask.resume()
        } else {
            completion(nil, NSError(domain: "Backend URL incorrect", code: -1, userInfo: nil))
        }
    }
}

extension NSMutableData {
    func append(_ string: String) {
        if let data = string.data(using: .utf8) {
            self.append(data)
        }
    }
}
