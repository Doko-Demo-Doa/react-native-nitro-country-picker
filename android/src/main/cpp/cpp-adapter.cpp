#include <jni.h>
#include "nitrocountrypickerOnLoad.hpp"

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* vm, void*) {
  return margelo::nitro::nitrocountrypicker::initialize(vm);
}
