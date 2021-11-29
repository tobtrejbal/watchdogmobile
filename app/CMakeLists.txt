# Sets the minimum version of CMake required to build the native
# library. You should either keep the default value or only pass a
# value of 3.4.0 or lower.

cmake_minimum_required(VERSION 3.4.1)

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds it for you.
# Gradle automatically packages shared libraries with your APK.

add_library( # Sets the name of the library.
             native-lib

             # Sets the library as a shared library.
             SHARED

             # Provides a relative path to your source file(s).
             # Associated headers in the same location as their source
             # file are automatically included.
             src/main/cpp/native-lib.cpp
             src/main/cpp/WatchDogCore.cpp
             src/main/cpp/Buffer.cpp
             src/main/cpp/Sample.cpp
             src/main/cpp/SensorValue.cpp
             src/main/cpp/SensorManager.cpp
             src/main/cpp/DatabaseManager.cpp
             src/main/cpp/CommunicationManager.cpp
             src/main/cpp/Monitor.cpp
             src/main/cpp/TransformerFB.cpp)

add_library( # Sets the name of the library.
             mqtt-paho

             # Sets the library as a shared library.
             SHARED

             # Provides a relative path to your source file(s).
             # Associated headers in the same location as their source
             # file are automatically included.
             src/main/cpp/paho/MQTTClient.c
             src/main/cpp/paho/MQTTProtocolClient.c
             src/main/cpp/paho/Clients.c
             src/main/cpp/paho/utf-8.c
             src/main/cpp/paho/StackTrace.c
             src/main/cpp/paho/MQTTPacket.c
             src/main/cpp/paho/MQTTPacketOut.c
             src/main/cpp/paho/Messages.c
             src/main/cpp/paho/Tree.c
             src/main/cpp/paho/Socket.c
             src/main/cpp/paho/Log.c
             src/main/cpp/paho/MQTTPersistence.c
             src/main/cpp/paho/Thread.c
             src/main/cpp/paho/MQTTProtocolOut.c
             src/main/cpp/paho/MQTTPersistenceDefault.c
             src/main/cpp/paho/SocketBuffer.c
             src/main/cpp/paho/Heap.c
             src/main/cpp/paho/LinkedList.c
             src/main/cpp/paho/VersionInfo.h)

# Searches for a specified prebuilt library and stores the path as a
# variable. Because system libraries are included in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( # Sets the name of the path variable.
              log-lib

              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log )

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in the
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
                       native-lib
                       # mqtt lib
                       mqtt-paho
                       # Links the target library to the log library
                       # included in the NDK.
                       android
                       ${log-lib} )