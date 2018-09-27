import 'dart:async';
import 'dart:convert';
import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class Foto {
  static const MethodChannel _channel = const MethodChannel('foto');

  static Future<List<File>> pickImageList({FotoOption option}) async {
    option ??= defaultOptions;
    var opt = json.encode({
      "themeColor": option.themeColor.value,
      "textColor": option.textColor.value,
      "dividerColor": option.dividerColor.value,
      "disableColor": option.disableColor.value,
      "rowCount": option.rowCount,
      "padding": option.padding,
      "maxSelected": option.maxSelected,
      "itemRadio": option.itemRadio,
    });
    var result = await _channel.invokeMethod("pickImage", opt);
    if (result == null) {
      return null; //取消或报错
    } else if (result is List<dynamic>) {
      return result.map((v) => File(v.toString())).toList();
    }
    return null;
  }

  static final FotoOption defaultOptions = FotoOption();
}

class FotoOption {
  final Color themeColor;

  final Color textColor;

  final Color dividerColor;

  final Color disableColor;

  final int rowCount;

  final int padding;

  final int maxSelected;

  final double itemRadio;

  FotoOption({
    this.themeColor = Colors.blue,
    this.textColor = Colors.white,
    Color dividerColor,
    Color disableColor,
    this.rowCount = 4,
    this.padding = 1,
    this.maxSelected = 9,
    this.itemRadio = 1.0,
  })  : this.dividerColor = dividerColor ?? Colors.grey.shade300,
        this.disableColor = disableColor ?? Colors.grey;
}
