require "json"

package = JSON.parse(File.read(File.join(__dir__, "package.json")))

Pod::Spec.new do |s|
  s.name         = "NitroCountryPicker"
  s.version      = package["version"]
  s.summary      = package["description"]
  s.homepage     = package["homepage"]
  s.license      = package["license"]
  s.authors      = package["author"]

  s.platforms    = { :ios => min_ios_version_supported }
  s.source       = { :git => "https://github.com/Doko-Demo-Doa/react-native-nitro-country-picker.git", :tag => "#{s.version}" }


  s.source_files = [
    "ios/**/*.{swift}",
    "ios/**/*.{m,mm}",
    "cpp/**/*.{hpp,cpp}",
  ]

  s.dependency 'React-jsi'
  s.dependency 'React-callinvoker'

  load 'nitrogen/generated/ios/NitroCountryPicker+autolinking.rb'
  add_nitrogen_files(s)

  # Ensure generated shared C++ headers are exported to Pods/Headers/Public.
  current_public_headers = Array(s.attributes_hash['public_header_files'])
  s.public_header_files = current_public_headers + [
    'nitrogen/generated/shared/c++/*.hpp',
  ]

  install_modules_dependencies(s)
end
