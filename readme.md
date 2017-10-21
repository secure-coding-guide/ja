# Androidアプリのセキュア設計・セキュアコーディングガイド

![](media/image1.png)

**2017年2月1日版**

**一般社団法人日本スマートフォンセキュリティ協会（JSSEC）**

**セキュアコーディングWG**

※　本ガイドの内容は執筆時点のものです。サンプルコードを使用する場合はこの点にあらかじめご注意ください。

※　JSSECならびに執筆関係者は、このガイド文書に関するいかなる責任も負うものではありません。全ては自己責任にてご活用ください。

> ※　Android™は、Google, Inc.の商標または登録商標です。また、本文書に登場する会社名、製品名、サービス名は、一般に各社の登録商標または商標です。本文中では®、TM、© マークは明記していません。
>
> ※　この文書の内容の一部は、Google, Inc.が作成、提供しているコンテンツをベースに複製したもので、クリエイティブ・コモンズの表示 3.0 ライセンスに記載の条件に従って使用しています。

<img src="media/image3.png" style="width:0.8131944444444444in; height:0.9840277777777777in;">

## Androidアプリのセキュア設計・セキュアコーディングガイド【ベータ版】

<img src="media/image4.png" style="width:0.8131944444444444in; height:0.9840277777777777in;">


2017年2月1日

一般社団法人日本スマートフォンセキュリティ協会

セキュアコーディングWG

### 目次

*Androidアプリのセキュア設計・セキュアコーディングガイド*

[*1.* *はじめに* 13](#はじめに)

[*1.1.* *スマートフォンを安心して利用出来る社会へ*](#スマートフォンを安心して利用出来る社会へ)

[*1.2.* *常にベータ版でタイムリーなフィードバックを*](#常にベータ版でタイムリーなフィードバックを)

[*1.3.* *本文書の利用許諾*](#本文書の利用許諾)

[*1.4.* *2016年9月1日版からの訂正記事について*](#年9月1日版からの訂正記事について)

[*2.* *ガイド文書の構成*](#ガイド文書の構成)

[*2.1.* *開発者コンテキスト*](#開発者コンテキスト)

[*2.2.* *サンプルコード、ルールブック、アドバンスト*](#サンプルコードルールブックアドバンスト)

[*2.3.* *ガイド文書のスコープ*](#ガイド文書のスコープ)

[*2.4.* *Androidセキュアコーディング関連書籍の紹介*](#androidセキュアコーディング関連書籍の紹介)

[*2.5.* *サンプルコードのAndroid Studioへの取り込み手順*](#サンプルコードのandroid-studioへの取り込み手順)

[*3.* *セキュア設計・セキュアコーディングの基礎知識*](#セキュア設計セキュアコーディングの基礎知識)

[*3.1.* *Androidアプリのセキュリティ*](#androidアプリのセキュリティ)

[*3.2.* *入力データの安全性を確認する*](#入力データの安全性を確認する)

[*4.* *安全にテクノロジーを活用する*](#安全にテクノロジーを活用する)

[*4.1.* *Activityを作る・利用する*](#activityを作る利用する)

[*4.2.* *Broadcastを受信する・送信する*](#broadcastを受信する送信する)

[*4.3.* *Content Providerを作る・利用する*](#content-providerを作る利用する)

[*4.4.* *Serviceを作る・利用する*](#serviceを作る利用する)

[*4.5.* *SQLiteを使う*](#sqliteを使う)

[*4.6.* *ファイルを扱う*](#ファイルを扱う)

[*4.7.* *Browsable Intentを利用する*](#browsable-intentを利用する)

[*4.8.* *LogCatにログ出力する*](#logcatにログ出力する)

[*4.9.* *WebViewを使う*](#webviewを使う)

[*4.10.* *Notificationを使用する*](#notificationを使用する)

[*5.* *セキュリティ機能の使い方*](#セキュリティ機能の使い方)

[*5.1.* *パスワード入力画面を作る*](#パスワード入力画面を作る)

[*5.2.* *PermissionとProtection Level*](#permissionとprotection-level)

[*5.3.* *Account Managerに独自アカウントを追加する*](#account-managerに独自アカウントを追加する)

[*5.4.* *HTTPSで通信する*](#httpsで通信する)

[*5.5.* *プライバシー情報を扱う*](#プライバシー情報を扱う)

[*5.6.* *暗号技術を利用する*](#暗号技術を利用する)

[*5.7.* *指紋認証機能を利用する*](#指紋認証機能を利用する)

[*6.* *難しい問題*](#難しい問題)

[*6.1.* *Clipboardから情報漏洩する危険性*](#clipboardから情報漏洩する危険性)

### 更新履歴
2012-06-01
-   初版

2012-11-01
* 下記の構成・内容を見直し拡充致しました
  - 4.1 Activityを作る・利用する
  - 4.2 Broadcastを受信する・送信する
  - 4.3 Content Providerを作る・利用する
  - 4.4 Serviceを作る・利用する
  - 5.2 PermissionとProtection Level
*   下記の新しい記事を追加致しました
     -   2.5 サンプルコードのAndroid Studioへの取り込み手順
     -   3.1 Androidアプリのセキュリティ
     -   4.7 Browsable Intentを利用する
     -   5.3 Account Managerに独自アカウントを追加する
     -   6.1 Clipboardから情報漏洩する危険性

2013-04-01     
*   下記の記事の内容を見直し書き直しました
     -   5.3 Account Managerに独自アカウントを追加する
*   下記の新しい記事を追加致しました
     -   4.8 LogCatにログ出力する
     -   5.4 HTTPSで通信する
     -   4.9 WebViewを使う

2014-07-01
*   下記の新しい記事を追加致しました
     -   5.5 プライバシー情報を扱う
     -   5.6　暗号技術を利用する

2015-06-01
*   下記の方針で本書全体の内容を見直し書き直しました
     -   開発環境の変更(Eclipse -&gt; Android Studio)
     -   Android最新版Lollipopへの対応
     -   対応するAPI Levelの見直し(8以降 -&gt; 15以降)

2016-02-01
*  下記の新しい記事を追加致しました
     -   4.10 Notificationを使用する
     -   5.7 指紋認証機能を利用する
 *   下記の構成・内容を見直し拡充致しました
     -   5.2 PermissionとProtection Level

2016-09-01
*   下記の構成・内容を見直し拡充致しました
     -   2.5 サンプルコードのAndroid Studioへの取り込み手順
     -   5.4 HTTPSで通信する
     -   5.6 暗号技術を利用する
  
2017-02-01
*   下記の新しい記事を追加致しました
     -   5.4.3.7 Network Security Configuration
     -   4.6.3.5 Android 7.0（API Level 24）における外部ストレージの特定ディレクトリへのアクセスに関する仕様変更について
*   下記の構成・内容を見直し拡充致しました
     -   4.1 Activityを作る・利用する
     -   4.2 Broadcastを受信する・送信する
     -   4.4 Serviceを作る・利用する
     -   4.5 SQLiteを使う
     -   4.6 ファイルを扱う
*   下記の記事を削除致しました
     -   4.8.3.4 BuildConfig.DEBUG はADT 21 以降で使う
*   下記の方針で本書全体の内容を見直し書き直しました
     -   Android 4.0.3（API Level 15）未満に関する本文中の記述を削除または脚注へ移動

※ 改訂内容の詳細については「1.4 2016年9月1日版からの訂正記事について」を参照して下さい

新版の公開にあたり、皆様から頂いたご意見・コメントを元に本ガイドの内容を更新しました。

■制作■

一般社団法人日本スマートフォンセキュリティ協会

技術部会　セキュアコーディングWG

|リーダー||
|-|-|
|奥山　謙|ソニーデジタルネットワークアプリケーションズ株式会社|

|メンバー||
|-|-|
|荒木　成治|Androidセキュリティ部|
|島野 映司|Androidセキュリティ部|
|大内　智美|株式会社SRA|
|福本　郁哉|株式会社SRA|
|山野井 陽一|株式会社SRA|
|武井　滋紀|エヌ・ティ・ティ・ソフトウェア株式会社|
|塩田 明弘|株式会社エヌ・ティ・ティ・データ|
|高橋 哲也|株式会社スクウェア・エニックス|
|山地　秀典|ソニー株式会社|
|安藤　彰|ソニーデジタルネットワークアプリケーションズ株式会社|
|小木曽　純|ソニーデジタルネットワークアプリケーションズ株式会社|
|松並 勝|ソニーデジタルネットワークアプリケーションズ株式会社|
|谷口　岳|タオソフトウェア株式会社|
||（執筆関係者、社名五十音順）|

■2016年9月1日版制作者■

|リーダー||
|-|-|
|松並　勝|ソニーデジタルネットワークアプリケーションズ株式会社|

|メンバー||
|-|-|
|荒木　成治|Androidセキュリティ部|
|大内　智美、福本　郁哉|株式会社SRA|
|武井　滋紀|エヌ・ティ・ティ・ソフトウェア株式会社|
|大園　通|シスコシステムズ合同会社|
|山地　秀典|ソニー株式会社|
|安藤　彰、大谷　三岳、小木曽　純、奥山　謙|ソニーデジタルネットワークアプリケーションズ株式会社|
|島野　英司、谷口　岳|タオソフトウェア株式会社|
|満園　大祐|日本システム株式会社|
||(執筆関係者、社名五十音順)|

■2016年2月1日版制作者■

|リーダー||
|-|-|
|松並　勝|ソニーデジタルネットワークアプリケーションズ株式会社|

|メンバー||
|-|-|
|安達　正臣|Androidセキュリティ部|
|福本　郁哉、星本　英史|株式会社SRA|
|武井　滋紀|エヌ・ティ・ティ・ソフトウェア株式会
|大園　通|シスコシステムズ合同会社|
|安藤　彰、伊藤 妙子、大谷　三岳、奥山　謙、楫 節子、西村　宗晃|ソニーデジタルネットワークアプリケーションズ株式会社|
|山地　秀典|ソニーモバイルコミュニケーションズ株式会社|
|笠原 正弘|ソフトバンクモバイル株式会社|
|島野　英司、谷口　岳|タオソフトウェア株式会社|
||(執筆関係者、社名五十音順)|

■2015年6月1日版制作者■

|リーダー||
|-|-|
|松並　勝|ソニーデジタルネットワークアプリケーションズ株式会社|

|メンバー||
|-|-|
|星本　英史|株式会社SRA|
|武井　滋紀|エヌ・ティ・ティ・ソフトウェア株式会社|
|大園　通|シスコシステムズ合同会社|
|安藤　彰、奥山　謙、西村　宗晃|ソニーデジタルネットワークアプリケーションズ株式会社|
|笠原 正弘|ソフトバンクモバイル株式会社|
|島野　英司、谷口　岳|タオソフトウェア株式会社|
|八津川　直伸|日本ユニシス株式会社|
|谷田部　茂|株式会社フォーマルハウト・テクノ・ソリューションズ|
|今西　杏丞、河原　豊、近藤　昭雄、志村　直彦、新谷　正人、原　昇平、藤澤　智之、藤田　竜史、三竹　一馬|株式会社ブリリアントサービス|
||(執筆関係者、社名五十音順)|

■2014年7月1日版制作者■

|リーダー||
|-|-|
|松並　勝|ソニーデジタルネットワークアプリケーションズ株式会社|

|メンバー||
|-|-|
|熊澤　努、星本　英史|株式会社SRA|
|武井　滋紀|エヌ・ティ・ティ・ソフトウェア株式会社|
|竹森　敬祐、磯原 隆将|KDDI株式会社|
|大園　通|シスコシステムズ合同会社|
|安藤　彰、伊藤 妙子、奥山　謙、楫 節子、片岡　良典|ソニーデジタルネットワークアプリケーションズ株式会社|
|笠原 正弘|ソフトバンクモバイル株式会社|
|島野　英司、谷口　岳|タオソフトウェア株式会社|
|佐藤　導吉|東京システムハウス株式会社|
|八津川　直伸|日本ユニシス株式会社|
|谷田部　茂|株式会社フォーマルハウト・テクノ・ソリューションズ|
||(執筆関係者、社名五十音順)|

■2013年4月1日版制作者■

|リーダー||
|-|-|
|松並　勝|ソニーデジタルネットワークアプリケーションズ株式会社|

|メンバー||
|-|-|
|安達　正臣、長谷川　智之|Androidセキュリティ部|
|安部　勇気、大内　智美、熊澤　努、澤田　寿実、畑　清志、比嘉　陽一、福井　悠、福本　郁哉、星本　英史、横井　俊、吉澤　孝和|株式会社SRA|
|藤原　健||NRIセキュアテクノロジーズ株式会社|
|武井　滋紀|エヌ・ティ・ティ・ソフトウェア株式会社|
|竹森　敬祐|KDDI株式会社|
|久保　正樹、熊谷　裕志、戸田　洋三|一般社団法人JPCERTコーディネーションセンター(JPCERT/CC)|
|大園　通|シスコシステムズ合同会社|
|新井　幹也、坂本　昌彦|株式会社セキュアスカイ・テクノロジー|
|浅野　徹、安藤　彰、池邉　亮志、小木曽　純、奥山　謙、片岡　良典、西村　宗晃、古澤　浩司、山岡　研二|ソニーデジタルネットワークアプリケーションズ株式会社|
|谷口　岳|タオソフトウェア株式会社|
|八津川　直伸|日本ユニシス株式会社|
|谷田部　茂|株式会社フォーマルハウト・テクノ・ソリューションズ|
||(執筆関係者、社名五十音順)|

■2012年11月1日版制作者■

|リーダー||
|-|-|
|松並　勝|ソニーデジタルネットワークアプリケーションズ株式会社|

|メンバー||
|-|-|
|佐藤　勝彦、中口　明彦|Androidセキュリティ部|
|大内　智美、大平　直之、熊澤　努、関川　未来、中野　正剛、比嘉　陽一、福本　郁哉、星本　英史、安田　章一、八尋　唯行、吉澤　孝和|株式会社SRA|
|武井　滋紀|エヌ・ティ・ティ・ソフトウェア株式会社|
|竹森　敬祐|KDDI株式会社|
|久保　正樹、熊谷　裕志、戸田　洋三|一般社団法人JPCERTコーディネーションセンター(JPCERT/CC)|
|大園　通|シスコシステムズ合同会社|
|浅野　徹、安藤　彰、池邉　亮志、市川　茂、大谷　三岳、小木曽　純、奥山　謙、片岡　良典、佐藤　郁恵、西村　宗晃、山岡　一夫、吉川　岳流|ソニーデジタルネットワークアプリケーションズ株式会社|
|谷口　岳、島野　英司、北村　久雄|タオソフトウェア株式会社|
|山川　隆郎|一般社団法人日本オンラインゲーム協会|
|石原　正樹、森　靖晃|日本システム開発株式会社|
|八津川　直伸|日本ユニシス株式会社|
|谷田部　茂|株式会社フォーマルハウト・テクノ・ソリューションズ|
|藤井　茂樹|ユニアデックス株式会社|
||(執筆関係者、社名五十音順)|

■2012年6月1日版制作者■

|リーダー||
|-|-|
|松並　勝|ソニーデジタルネットワークアプリケーションズ株式会社|

|メンバー||
|-|-|
|佐藤　勝彦|Androidセキュリティ部|
|大内　智美、比嘉　陽一、星本　英史|株式会社SRA|
|武井　滋紀|エヌ・ティ・ティ・ソフトウェア株式会社|
|千田　雅明|グリー株式会社|
|久保　正樹、熊谷　裕志、戸田　洋三|一般社団法人JPCERTコーディネーションセンター(JPCERT/CC)|
|大園　通、谷田部　茂|シスコシステムズ合同会社|
|田口　陽一|株式会社システムハウス. アイエヌジー|
|坂本　昌彦|株式会社セキュアスカイ・テクノロジー|
|安藤　彰、市川　茂、奥山　謙、佐藤　郁恵、西村　宗晃、山岡　一夫|ソニーデジタルネットワークアプリケーションズ株式会社|
|谷口　岳、島野　英司、北村　久雄|タオソフトウェア株式会社|
|佐藤　導吉|東京システムハウス株式会社|
|服部　正和|トレンドマイクロ株式会社|
|八津川　直伸|日本ユニシス株式会社|
|谷田部　茂|株式会社フォーマルハウト・テクノ・ソリューションズ|
|藤井　茂樹|ユニアデックス株式会社|
||(執筆関係者、社名五十音順)|

はじめに
========

スマートフォンを安心して利用出来る社会へ
----------------------------------------

本ガイドはAndroidアプリケーション開発者向けのセキュア設計、セキュアコーディングのノウハウをまとめたTips集です。できるだけ多くのAndroidアプリケーション開発者に活用していただきたく思い、ここに公開いたします。

昨今、スマートフォン市場は急拡大しており、さらにその勢いは増すばかりです。スマートフォン市場の急拡大は多種多彩なアプリケーション群によってもたらされています。従来の携帯電話ではセキュリティ制約によって利用できなかったさまざまな携帯電話の重要な機能がスマートフォンアプリケーションには開放され、従来の携帯電話では実現できなかった多種多彩なアプリケーション群がスマートフォンの魅力を引き立てています。

スマートフォンのアプリケーション開発者にはそれ相応の責任が生じています。従来の携帯電話ではあらかじめ課せられたセキュリティ制約によって、セキュリティについてあまり意識せずに開発したアプリケーションであっても比較的安全性が保たれていました。スマートフォンでは前述のとおり、携帯電話の重要な機能がアプリケーション開発者に開放されているため、アプリケーション開発者がセキュリティを意識して設計、コーディングをしなければ、スマートフォン利用者の個人情報が漏洩したり、料金の発生する携帯電話機能をマルウェアに悪用されたりといった被害が生じます。

AndroidスマートフォンはiPhoneに比べると、アプリケーション開発者のセキュリティへの配慮がより多く求められます。iPhoneに比べAndroidスマートフォンはアプリケーション開発者に開放された携帯電話機能が多く、App
Storeに比べGoogle Play（旧Android
Market）は無審査でアプリケーション公開ができるなど、アプリケーションのセキュリティがほぼ全面的にアプリケーション開発者に任されているためです。

スマートフォン市場の急拡大にともない、様々な分野のソフトウェア技術者が一気にスマートフォンアプリケーション開発市場に流れ込んできており、スマートフォン特有のセキュリティを考慮したセキュア設計、セキュアコーディングのノウハウ集約、共有が急務となっています。

このような状況を踏まえ、一般社団法人日本スマートフォンセキュリティ協会はセキュアコーディングWGを立ち上げ、Androidアプリケーションのセキュア設計、セキュアコーディングのノウハウを集めて、公開することにいたしました。それがこのガイド文書です。多くのAndroidアプリケーション開発者にセキュア設計、セキュアコーディングのノウハウを知っていただき、アプリケーション開発に活かしていただくことで、市場にリリースされる多くのAndroidアプリケーションのセキュリティを高めることを狙っています。その結果、安心、安全なスマートフォン社会づくりに貢献したいと考えています。

常にベータ版でタイムリーなフィードバックを
------------------------------------------

私たちJSSECセキュアコーディングWGはこのガイド文書の内容について、できるだけ間違いがないように心がけておりますが、その正しさを保証するものではありません。私たちはタイムリーにノウハウを公開し共有していくことが第一と考え、最新かつその時点で正しいと思われることをできるだけ記載・公開し、間違いがあればフィードバックを頂いて常に正しい情報に更新し、タイムリーに提供するよう心がける、いわゆる常にベータ版というアプローチをとっています。このアプローチはこのガイド文書をご利用いただく多くのAndroidアプリケーション開発者のみなさまにとって有意義であると私たちは信じています。

このガイド文書とサンプルコードの最新版はいつでも下記URLから入手できます。

-   [*http://www.jssec.org/dl/android\_securecoding.pdf*](http://www.jssec.org/dl/android_securecoding.pdf)
    ガイド文書

-   [*http://www.jssec.org/dl/android\_securecoding.zip*](http://www.jssec.org/dl/android_securecoding.zip)
    サンプルコード一式

本文書の利用許諾
----------------

このガイド文書のご利用に際しては次の2つの注意事項に同意いただく必要がございます。

1.  このガイド文書には間違いが含まれている可能性があります。ご自身の責任のもとでご利用ください。

2.  このガイド文書に含まれる間違いを見つけた場合には、下記連絡先までメールにてご連絡ください。ただしお返事することや修正をお約束するものではありませんのでご了承ください。

一般社団法人 日本スマートフォンセキュリティ協会

セキュアコーディングWG問い合わせ

メール宛先：
[*jssec-securecoding-qa@googlegroups.com*](mailto:jssec-securecoding-qa@googlegroups.com)

件名：【コメント応募】Androidアプリのセキュア設計・セキュアコーディングガイド
2016年9月1日版

内容：氏名(任意)/所属(任意)/連絡先E-mail(任意)/ご意見(必須)/その他ご希望(任意)


2016年9月1日版からの訂正記事について
------------------------------------

本節では、前版の記事について事実関係と照らし合わせることで判明した訂正事項を一覧にして掲載しています。各訂正記事は、執筆者による継続的な調査結果だけでなく読者の方々の貴重なご指摘を広く取り入れたものです。特に、いただいたご指摘は、本改訂版をより実践に即したガイドとして高い完成度を得るための最も重要な糧となっています。

前版を元にアプリケーション開発を進めていた読者は、以下の訂正記事一覧に特に目を通していただきますようお願いいたします。なお、ここで掲げる項目には、誤植の修正、記事の追加、構成の変更、単なる表現上の改善は含みません。

本ガイドに対するコメントは、今後もお気軽にお寄せくださいますようよろしくお願いいたします。

**訂正記事一覧**

|2016年9月1日版の修正個所|本改訂版の訂正記事|訂正の要旨|
|-|-|-|
|4.1.3.1 exported設定とintent-filter設定の組み合わせ（Activityの場合）|4.1.3.1 exported 設定とintent-filter設定の組み合わせ(Activityの場合)|intent-filter定義の有無に関わりなくActivityのexported属性を指定すべきである旨を追記しました。|
|4.2.3.1 使用してよいexported設定とintent-filter設定の組み合わせ（Receiverの場合）|4.2.3.1 使用してよいexported 設定とintent-filter設定の組み合わせ(Receiverの場合)|intent-filter定義の有無に関わりなくReceiverのexported属性を指定すべきである旨を追記しました。|
|4.4.3.1 exported設定とintent-filter設定の組み合わせ（Serviceの場合）|4.4.3.1 exported 設定とintent-filter設定の組み合わせ(Serviceの場合)|intent-filter定義の有無に関わりなくServiceのexported属性を指定すべきである旨を追記しました。|
|（全般）|（全般）|Android 4.0.3（API Level 15）未満に関する記述を削除もしくは脚注に移動しました。|
|（該当なし）|5.4.3.7 Network Security Configuration|Network Security Configurationについての解説を記載しました。|
|4.8.3.4 BuildConfig.DEBUG はADT 21 以降で使う|（該当なし）|ADT に関する記述を削除しました。|
|4.2.2.6 Sticky Broadcastにはセンシティブな情報は含めない|4.2.2.6　Sticky Broadcastにはセンシティブな情報は含めない|Sticky Broadcastの使用がAPI Level 21以降非推奨となった旨を追記しました。|
|4.2.3.4　Broadcastの種類とその特徴|4.2.3.4　Broadcastの種類とその特徴|Sticky Broadcastの使用がAPI Level 21以降非推奨となった旨を追記しました。|
|4.5.2.1 DBファイルの配置場所、アクセス権を正しく設定する|4.5.2.1 DBファイルの配置場所、アクセス権を正しく設定する|MODE\_WORLD\_READABLEおよびMODE\_WORLD\_WRITEABLEの使用が API Level17 以降非推奨となった旨を追記しました。|
|4.6.3.2 ディレクトリのアクセス権設定|4.6.3.2 ディレクトリのアクセス権設定|MODE\_WORLD\_READABLEおよびMODE\_WORLD\_WRITEABLEの使用が API Level17 以降非推奨となった旨を追記しました。|
|（該当なし）|4.6.3.5　Android 7.0（API Level 24）における外部ストレージの特定ディレクトリへのアクセスに関する仕様変更について|外部ストレージへのアクセスに関する仕様がAPI Level 19において変更されたことについての解説を記載しました。|
