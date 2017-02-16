#pragma once

#include <windows.h>    
#include <windowsx.h>  

#include <jni.h>

#include <thread>
#include <map>
#include <iostream>


extern "C" {
	JNIEXPORT void JNICALL Java_ca_utoronto_utm_pointer_WindowsPointer_init(JNIEnv *, jobject, jlong);
}

const int CONTROL = 1 << 1; //2
const int SHIFT = 1 << 0; //1
const int ALT = 1 << 3; //8
const int LBUTTON = 1 << 4; //16
const int RBUTTON = 1 << 2; //4
const int MBUTTON = 1 << 3; //8

const int JMOUSE_FIRST = 500;
const int JMOUSE_LAST = 507;
const int JMOUSE_PRESSED = 1 + JMOUSE_FIRST;
const int JMOUSE_RELEASED = 2 + JMOUSE_FIRST;
const int JMOUSE_MOVED = 3 + JMOUSE_FIRST;
const int JMOUSE_ENTERED = 4 + JMOUSE_FIRST;
const int JMOUSE_EXITED = 5 + JMOUSE_FIRST;
const int JKEY_PRESS = 401;
const int JKEY_RELEASE = 402;



class WindowsProc
{
public:
	WindowsProc(JavaVM *jvm, jobject object, WNDPROC wndprocOrig);
	~WindowsProc();

	static std::map <HWND, WindowsProc*> windObjects;

	static LRESULT CALLBACK PtrSetup(HWND hWnd, UINT message, WPARAM wParam, LPARAM lParam);
	static LRESULT CALLBACK WndProcProxy(HWND hWnd, UINT message, WPARAM wParam, LPARAM lParam);

private:
	JavaVM *jvm;
	jobject pairedObject;
	WNDPROC wndprocOrig;
	jmethodID update, key;
	void getMethods();
	jint getButton(POINTER_FLAGS flags);
	void sendKeyUpdate(int eventId, int modifiers, WPARAM wParam);
	void sendUpdate(int eventId, POINTER_INFO pointerInfo, int modifiers, int pressure);
	LRESULT _PtrSetup(HWND hWnd, UINT message, WPARAM wParam, LPARAM lParam);
	LRESULT _WndProcProxy(HWND hWnd, UINT message, WPARAM wParam, LPARAM lParam);

	static jint getModifiers();
};

