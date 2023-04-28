LOCAL_PATH:=$(call my-dir)
include $(CLEAR_VARS)
# so's name
LOCAL_MODULE:=crypt_tool
# target c file
LOCAL_SRC_FILES:=crypt_tool.c
#shared so lib
include $(BUILD_SHARED_LIBRARY)
