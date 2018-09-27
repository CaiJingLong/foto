import 'dart:async';

import 'package:flutter/services.dart';

class Foto {
  static const MethodChannel _channel =
      const MethodChannel('foto');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }
}
