import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:foto/foto.dart';

void main() => runApp(new MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => new _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return new MaterialApp(
      home: new Scaffold(
        appBar: new AppBar(
          title: const Text('Plugin example app'),
        ),
        body: new Center(
          child: new Text('Running on: $_platformVersion\n'),
        ),
        floatingActionButton: FloatingActionButton(
          onPressed: _pickImage,
        ),
      ),
    );
  }

  void _pickImage() async {
    var resultList = await Foto.pickImageList(
      option: FotoOption(
        itemRadio: 1.0,
        padding: 1,
        maxSelected: 6,
        themeColor: Colors.green,
        rowCount: 5,
        dividerColor: Colors.red,
        textColor: Colors.yellowAccent,
      ),
    );
    if (resultList == null) {
      print("取消");
    } else {
      print("获取结果为 $resultList");
    }
  }
}
