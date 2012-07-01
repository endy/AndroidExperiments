
LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := native_test
LOCAL_SRC_FILES := native_test.c
LOCAL_LDLIBS    := -llog -lEGL -lGLESv2

include $(BUILD_SHARED_LIBRARY)


