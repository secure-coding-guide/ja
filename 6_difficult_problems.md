難しい問題
==========

AndroidにはOSの仕様やOSが提供する機能の仕様上、アプリの実装でセキュリティを担保するのが困難な問題が存在する。これらの機能は悪意を持った第三者に悪用されたり、ユーザーが注意せずに利用したりすることで、情報漏洩を始めセキュリティ上の問題に繋がってしまう危険性を常に抱えている。この章ではそのような機能に対して、開発者が取りうるリスク削減策などを提示しながら注意喚起が必要な話題を記事として取り上げる。

Clipboardから情報漏洩する危険性
-------------------------------

コピー＆ペーストはユーザーが普段から何気なく使っている機能であろう。例えば、この機能を使って、メールやWebページで気になった情報や忘れたら困る情報をメモ帳に残しておいたり、設定したパスワードを忘れないようにメモ帳に保存しておき、必要な時にコピー＆ペーストして使うというユーザーは少なからず存在する。これらは一見何気ない行為であるが、実はユーザーの扱う情報が盗まれるという危険が潜んでいる。

これにはAndroidのコピー＆ペーストの仕組みが関係している。ユーザーやアプリによってコピーされた情報は、一旦Clipboardと呼ばれるバッファに格納される。ユーザーやアプリによってペーストされたときに、このClipboardの内容が各アプリに再配布されるわけである。このClipboardに情報漏洩に結び付く危険性がある。Android端末の仕様では、Clipboardの実体は端末に1つであり、ClipboardManagerを利用することで、どのアプリからでも常時Clipboardの中身が取得できるようになっているからである。このことは、ユーザーがコピー・カットした情報は全て悪意あるアプリに対して筒抜けになることを意味している。

よって、アプリ開発者は、このAndroidの仕様を考慮しながら情報漏洩の可能性を最小限に抑える対策を講じなくてはならない。

### サンプルコード

Clipboardから情報漏洩する可能性を抑える対策には、大きく分けて次の2つが考えられる。

(1) 他アプリから自アプリへコピーする際の対策

(2) 自アプリから他アプリへコピーする際の対策

最初に、1.について説明する。ここでは、ユーザーがメモ帳やWebブラウザ、メーラーアプリなど他アプリから文字列をコピーし、それを自アプリのEditTextに貼り付けるシナリオを想定している。結論だけを言ってしまうと、このシナリオでコピー・カットによってセンシティブな情報が漏洩してしまうことを防ぐ根本的な対策は存在しない。第三者アプリのコピー機能を制御するような機能がAndroidにはないからだ。

よって、1.についてはセンシティブな情報をコピー・カットする危険性をユーザーに説明し、行為自体を減らしていく啓発活動を継続的に行っていくしか対策はない。

次に、2.を説明する。ここでは、自アプリが表示している情報がユーザーによってコピーされるシナリオを想定する。この場合、漏洩に対する確実な対策は、View(TextView,
EditTextなど)からのコピー・カットを禁止にすることである。個人情報などセンシティブな情報が入力あるいは出力されるViewにコピー・カット機能がなければ、自アプリからのClipboardを介した情報の漏洩もないからだ。

コピー・カットを禁止する方法はいくつか考えられるが、ここでは、実装が簡単でかつ効果のある方法として、Viewの長押し無効化の方法と文字列選択時のメニューからコピー・カットの項目を削除する方法を扱う。

対策要否は、図 6.1‑1の判定フローによって判定することができる。図
6.1‑1において、入力タイプ(Input
Type)がPassword属性に固定されているとは、入力タイプ(Input
Type)がアプリの実行時に常に下記のいずれかであることを指す。この場合は、デフォルトでコピー・カットが禁止されているので、特に対策する必要はない。

-   InputType.TYPE\_CLASS\_TEXT |
    InputType.TYPE\_TEXT\_VARIATION\_PASSWORD

-   InputType.TYPE\_CLASS\_TEXT |
    InputType.TYPE\_TEXT\_VARIATION\_WEB\_PASSWORD

-   InputType.TYPE\_CLASS\_NUMBER |
    InputType.TYPE\_NUMBER\_VARIATION\_PASSWORD

![](media/image91.png){width="3.9358267716535433in"
height="3.6429133858267715in"}

[[]{#_Ref350183803 .anchor}]{#_Ref350183809 .anchor}図
6.1‑1対策要否の判定フロー

以下で、それぞれの対策の詳細を説明し、サンプルコードを示す。

#### 文字列選択時のメニューからコピー・カットを削除する

TextView.setCustomSelectionActionModeCallback()メソッドによって、文字列選択時のメニューをカスタマイズできる。これを用いて、文字列選択時のメニューからコピー・カットのアイテムを削除すれば、ユーザーが文字列をコピー・カットすることはできなくなる。

以下、EditTextの文字列選択時のメニューからコピー・カットの項目を削除するサンプルコードを示す。

> ポイント：

1.  文字列選択時のメニューからandroid.R.id.copyを削除する。

&nbsp;
1.  文字列選択時のメニューからandroid.R.id.cutを削除する。

> UncopyableActivity.java

package org.jssec.android.clipboard.leakage;

import android.app.Activity;

import android.os.Bundle;

import android.support.v4.app.NavUtils;

import android.view.ActionMode;

import android.view.Menu;

import android.view.MenuItem;

import android.widget.EditText;

public class UncopyableActivity extends Activity {

private EditText copyableEdit;

private EditText uncopyableEdit;

@Override

public void onCreate(Bundle savedInstanceState) {

super.onCreate(savedInstanceState);

setContentView(R.layout.uncopyable);

copyableEdit = (EditText) findViewById(R.id.copyable\_edit);

uncopyableEdit = (EditText) findViewById(R.id.uncopyable\_edit);

// setCustomSelectionActionModeCallbackメソッドにより、

// 文字列選択時のメニューをカスタマイズすることができる。

uncopyableEdit.setCustomSelectionActionModeCallback(actionModeCallback);

}

private ActionMode.Callback actionModeCallback = new
ActionMode.Callback() {

public boolean onPrepareActionMode(ActionMode mode, Menu menu) {

return false;

}

public void onDestroyActionMode(ActionMode mode) {

}

public boolean onCreateActionMode(ActionMode mode, Menu menu) {

// ★ポイント1★ 文字列選択時のメニューからandroid.R.id.copyを削除する。

MenuItem itemCopy = menu.findItem(android.R.id.copy);

if (itemCopy != null) {

menu.removeItem(android.R.id.copy);

}

// ★ポイント2★ 文字列選択時のメニューからandroid.R.id.cut削除する。

MenuItem itemCut = menu.findItem(android.R.id.cut);

if (itemCut != null) {

menu.removeItem(android.R.id.cut);

}

return true;

}

public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

return false;

}

};

@Override

public boolean onCreateOptionsMenu(Menu menu) {

getMenuInflater().inflate(R.menu.uncopyable, menu);

return true;

}

@Override

public boolean onOptionsItemSelected(MenuItem item) {

switch (item.getItemId()) {

case android.R.id.home:

NavUtils.navigateUpFromSameTask(this);

return true;

}

return super.onOptionsItemSelected(item);

}

}

#### Viewの長押し(Long Click)を無効にする

コピー・カットを禁止する方法は、Viewの長押し(Long
Click)を無効にすることでも実現できる。Viewの長押し無効化はレイアウトのxmlファイルで指定することができる。

> ポイント：

1.  コピー･カットを禁止するViewはandroid:longClickableをfalseにする。

> unlongclickable.xml

&lt;LinearLayout
xmlns:android="http://schemas.android.com/apk/res/android"

xmlns:tools="http://schemas.android.com/tools"

android:layout\_width="match\_parent"

android:layout\_height="match\_parent"

android:orientation="vertical"&gt;

&lt;TextView

android:layout\_width="match\_parent"

android:layout\_height="wrap\_content"

android:text="@string/unlongclickable\_description" /&gt;

&lt;!-- コピー・カットを禁止するEditText --&gt;

&lt;!-- ★ポイント1★
コピー・カットを禁止するViewはandroid:longClickableをfalseにする。
--&gt;

&lt;EditText

android:layout\_width="match\_parent"

android:layout\_height="wrap\_content"

android:longClickable="false"

android:hint="@string/unlongclickable\_hint" /&gt;

&lt;/LinearLayout&gt;

### ルールブック

自アプリから他アプリへのセンシティブな情報のコピーが発生する可能性がある場合は、以下のルールを守ること。

1.  Viewに表示されている文字列のコピー・カットを無効にする (必須)

#### Viewに表示されている文字列のコピー・カットを無効にする (必須)

アプリがセンシティブな情報を表示するViewを持っている場合、それがEditTextのようにコピー・カットが可能なViewならば、Clipboardを介してその情報が漏洩してしまう可能性がある。そのため、センシティブな情報を表示するViewはコピー・カットを無効にしておかなければならない。

コピー・カットを無効にする方法には、文字列選択時のメニューからコピー・カットの項目を削除する方法と、Viewの長押しを無効化する方法がある。

「6.1.3.1 ルール適用の際の注意」も参照のこと。

### アドバンスト

#### ルール適用の際の注意

TextViewはデフォルトでは文字列選択不可であるため、通常は対策不要であるが、アプリの仕様によってはコピーを可能にする場合もある。TextView.setTextIsSelectable()メソッドを使うことで、文字列の選択可否とコピー可否を動的に設定することができる。TextViewをコピー可能とする場合は、そのTextViewにセンシティブな情報が表示される可能性がないかよく検討し、その可能性があるのであれば、コピー可にすべきでない。

また、「6.1.1
サンプルコード」の判定フローにも記載されているように、パスワードの入力を想定した入力タイプ(InputType.TYPE\_CLASS\_TEXT
|
InputType.TYPE\_TEXT\_VARIATION\_PASSWORDなど)のEditTextについては、デフォルトで文字列のコピーが禁止されているため通常は対策不要である。しかし、「5.1.2.2.
パスワードを平文表示するオプションを用意する
（必須）」に記載したように「パスワードを平文表示する」オプションを用意している場合は、パスワード平文表示の際に入力タイプが変化し、コピー・カットが有効になってしまうので、同様の対策が必要である。

なお、ルールを適用する際には、ユーザビリティの面も考慮する必要があるだろう。例えば、ユーザーが自由にテキストを入力できるViewの場合、センシティブな情報が入力される「可能性がゼロでない」からといってコピー・カットを無効にしてしまったら、ユーザーの使い勝手が悪くなるだろう。もちろん、重要度の高い情報を入出力するViewやセンシティブな情報を単独で入力するようなViewにはルールを無条件で適用するべきであるが、それ以外のViewを扱う場合は、次のことを考慮しながら対応を考えると良い。

-   センシティブな情報の入力や表示を行う専用のコンポーネントを用意できないか

-   連携先(ペースト先)アプリが分かっている場合は、他の方法で情報を送信できないか

-   アプリでユーザーに入出力に関する注意喚起ができないか

-   本当にそのViewが必要か

Android
OSのClipboardとClipboardManagerの仕様にセキュリティに対する考慮がされていないことが情報漏洩の可能性を生む根本的な要因ではあるが、アプリ開発者は、ユーザー保護やユーザビリティ、提供する機能など様々な観点からこうしたClipboardの仕様に対して対応し、質の高いアプリを作成する必要がある。

#### Clipboardに格納されている情報の操作

> 「6.1
> Clipboardから情報漏洩する危険性」で述べたように、ClipboardManagerを利用することでアプリからClipboardに格納された情報を操作することができる。また、ClipboardManagerの利用には特別なPermissionを設定する必要が無いため、アプリはユーザーに知られることなくClipboardManagerを利用できる。
>
> Clipboardに格納されている情報(ClipDataと呼ぶ)は、ClipboardManager.getPrimaryClip()メソッドによって取得できる。タイミングに関しても、OnPrimaryClipChangedListenerを実装してClipboardManager.addPrimaryClipChangedListener()メソッドでClipboardManagerに登録すれば、ユーザーの操作などにより発生するコピー・カットの度にListenerが呼び出されるので、タイミングを逃すことなくClipDataを取得することができる。ここでListenerの呼び出しは、どのアプリでコピー・カットが発生したかに関係なく行われる。

以下、端末内でコピー・カットが発生する度にClipDataを取得し、Toastで表示するServiceのソースコードを示す。下記のような簡単なコードによりClipboardに格納された情報が筒抜けになってしまうことを実感していただきたい。アプリを実装する際は、少なくとも下記のコードによってセンシティブな情報が取得されてしまうことのないように注意する必要がある。

> ClipboardListeningService.java

package org.jssec.android.clipboard;

import android.app.Service;

import android.content.ClipData;

import android.content.ClipboardManager;

import android.content.ClipboardManager.OnPrimaryClipChangedListener;

import android.content.Context;

import android.content.Intent;

import android.os.IBinder;

import android.util.Log;

import android.widget.Toast;

public class ClipboardListeningService extends Service {

private static final String TAG = "ClipboardListeningService";

private ClipboardManager mClipboardManager;

@Override

public IBinder onBind(Intent arg0) {

return null;

}

@Override

public void onCreate() {

super.onCreate();

mClipboardManager = (ClipboardManager)
getSystemService(Context.CLIPBOARD\_SERVICE);

if (mClipboardManager != null) {

mClipboardManager.addPrimaryClipChangedListener(clipListener);

} else {

Log.e(TAG,
"ClipboardServiceの取得に失敗しました。サービスを終了します。");

this.stopSelf();

}

}

@Override

public void onDestroy() {

super.onDestroy();

if (mClipboardManager != null) {

mClipboardManager.removePrimaryClipChangedListener(clipListener);

}

}

private OnPrimaryClipChangedListener clipListener = new
OnPrimaryClipChangedListener() {

public void onPrimaryClipChanged() {

if (mClipboardManager != null && mClipboardManager.hasPrimaryClip()) {

ClipData data = mClipboardManager.getPrimaryClip();

ClipData.Item item = data.getItemAt(0);

Toast

.makeText(

getApplicationContext(),

"コピーあるいはカットされた文字列:\\n"

+ item.coerceToText(getApplicationContext()),

Toast.LENGTH\_SHORT)

.show();

}

}

};

}

次に、上記ClipboardListeningServiceを利用するActivityのソースコードの例を示す。

> ClipboardListeningActivity.java

package org.jssec.android.clipboard;

import android.app.Activity;

import android.content.ComponentName;

import android.content.Intent;

import android.os.Bundle;

import android.util.Log;

import android.view.View;

import android.widget.Toast;

public class ClipboardListeningActivity extends Activity {

private static final String TAG = "ClipboardListeningActivity";

@Override

public void onCreate(Bundle savedInstanceState) {

super.onCreate(savedInstanceState);

setContentView(R.layout.activity\_clipboard\_listening);

}

public void onClickStartService(View view) {

if (view.getId() != R.id.start\_service\_button) {

Log.w(TAG, "View IDが不正です");

} else {

ComponentName cn = startService(

new Intent(ClipboardListeningActivity.this,
ClipboardListeningService.class));

if (cn == null) {

Log.e(TAG, "サービスの起動に失敗しました");

Toast.makeText(this, "サービスの起動に失敗しました",
Toast.LENGTH\_SHORT).show();

}

}

}

public void onClickStopService(View view) {

if (view.getId() != R.id.stop\_service\_button) {

Log.w(TAG, "View IDが不正です");

} else {

stopService(new Intent(ClipboardListeningActivity.this,
ClipboardListeningService.class));

}

}

}

ここまでは、Clipboardに格納された情報を取得する方法について述べたが、ClipboardManager.setPrimaryClip()メソッドによって、Clipboardに新しく情報を格納することも可能である。

ただし、setPrimaryClip()はClipboardに格納されていた情報を上書きするので、ユーザーが予めコピー・カット操作により格納しておいた情報が失われる可能性がある点に注意が必要である。これらのメソッドを使用して独自のコピー機能あるいはカット機能を提供する場合は、必要に応じて、内容が改変される旨を警告するダイアログを表示するなど、Clipboardに格納されている内容がユーザーの意図しない内容に変更されることのないように設計・実装する必要がある。

[^1]: ただし、ポイント1, 2,
    6を遵守している場合を除いてはIntentが第三者に読み取られるおそれがあることに注意する必要がある。詳細はルールブックセクションの4.1.2.2、4.1.2.3を参照すること。

[^2]: 江川、藤井、麻野、藤田、山田、山岡、佐野、竹端著「Google Android
    プログラミング入門」 (アスキー・メディアワークス、2009年7月)

[^3]: [*http://developer.android.com/guide/components/tasks-and-back-stack.html*](http://developer.android.com/guide/components/tasks-and-back-stack.html)

[^4]: intent-filterが定義されていれば公開Activity、定義されていなければ非公開Activityとなる。
    https://developer.android.com/guide/topics/manifest/activity-element.html\#exported
    を参照のこと。

[^5]: Fragment Injectionの詳細は以下のURLを参照のこと

    https://securityintelligence.com/new-vulnerability-android-framework-fragment-injection/

[^6]: intent-filterが定義されていれば公開Receiver、定義されていなければ非公開Receiverとなる。
    https://developer.android.com/guide/topics/manifest/receiver-element.html\#exported
    を参照のこと。

[^7]: Android
    3.0未満ではアプリのインストールをしただけでReceiverが登録される

[^8]: ただし、Content Providerの非公開設定はAndroid 2.2 (API Level 8)
    以前では機能しない。

[^9]: intent-filterが定義されていれば公開Service、定義されていなければ非公開Serviceとなる。
    https://developer.android.com/guide/topics/manifest/service-element.html\#exported
    を参照のこと。

[^10]: ファイルの配置に関しては、SQLiteOpenHelperのコンストラクタの第2引数（name）にファイルの絶対パスも指定できる。そのため、誤ってSDカードを直接指定した場合には他のアプリからの読み書きが可能になるので注意が必要である。

[^11]: どちらのメソッドも該当するアプリだけが読み書き権限を与えられ、他のアプリからはアクセスができないディレクトリ（パッケージディレクトリ）のサブディレクトリ以下のパスが取得できる。

[^12]: （ドキュメントに記述はないが）SQLiteOpenHelper
    の実装ではDBの名前にはファイルのフルパスを指定できるので、SDカードなどアクセス権の設定できない場所のパスが意図せず入力されないように注意が必要である。

[^13]: MODE\_WORLD\_READABLEおよびMODE\_WORLD\_WRITEABLEの性質と注意点については、「4.6.3.2
    ディレクトリのアクセス権設定」を参照

[^14]: getReableDatabase
    は基本的にはgetWritableDatabaseで取得するのと同じオブジェクトを返す。ディスクフルなどの状況で書き込み可能オブジェクトを生成できない場合にリードオンリーのオブジェクトを返すという仕様である（getWritableDatabaseはディスクフルなどの状況では実行エラーとなる）。

[^15]: MODE\_WORLD\_READABLEおよびMODE\_WORLD\_WRITEABLEは API Level17
    以降ではdeprecated となっており、API Level 24
    以降ではセキュリティ例外が発生するため使用できなくなっている。

[^16]: 内部ストレージから外部記憶装置(SDカードなど)への移動などマウントポイントを超えた移動はできない。そのため、読み取り権限のない内部ストレージファイルが外部記憶装置に移動されて読み書き可能になるようなことはない。

[^17]: LogCat に出力されたログ情報は、READ\_LOGS
    Permissionを利用宣言したアプリであれば読み取り可能である。ただしAndroid
    4.1 以降ではLogCat
    に出力された他のアプリのログ情報は読み取り不可となった。また、スマートフォンユーザーであれば、ADB
    経由でLogCat のログ情報を参照することも可能である。

[^18]: http://developer.android.com/intl/ja/reference/android/util/Log.html

[^19]: 前述のサンプルコードを、条件式にBuildConfig.DEBUGを用いたif文で囲った。Log.d()呼び出し前のif文は不要であるが、前述のサンプルコードと対比させるため、そのまま残した。

[^20]: 厳密に言えば安全性を保証できるコンテンツであればJavaScriptを有効にしてよい。自社管理のコンテンツであれば自社の努力で安全性を確保できるし責任も取れる。では信頼できる提携会社のコンテンツは安全だろうか？これは会社間の信頼関係により決まる。信頼できる提携会社のコンテンツを安全であると信頼してJavaScriptを有効にしてもよいが、万一の場合は自社責任も伴うため、ビジネス責任者の判断が必要となる。

[^21]: http://www.w3.org/TR/webmessaging/

[^22]: オリジンとは、URLのスキーム、ホスト名、ポート番号の組み合わせのこと。詳細な定義は[*http://tools.ietf.org/html/rfc6454*](http://tools.ietf.org/html/rfc6454)を参照。

[^23]: Uri.EMPTYおよびUri.parse("")がワイルドカードとして機能する(2016年9月1日版執筆時)

[^24]: Android 6.0(API Level
    23)以降では、ユーザー確認と権限の付与はインストール時に行われず、アプリの実行中に権限の利用を要求する仕様に変更された。詳細は「5.2.1.4
    Android 6.0以降でDangerous Permissionを利用する方法」および「5.2.3.6
    Android
    6.0以降のPermissionモデルの仕様変更について」を参照すること。

[^25]: Normal/Dangerous
    Permissionを利用する場合には、Permissionが未定義のまま利用側アプリが先にインストールされると、利用側アプリへの権限の付与が行われず、提供側アプリがインストールされた後もアクセスができない

[^26]: Normal PermissionおよびSignature PermissionはAndroid
    OSにより自動的に付与されるため、ユーザー確認を行う必要はない。

[^27]: この場合も、アプリによるandroid.permission.READ\_CALENDARとandroid.permission.WRITE\_CALENDARの利用宣言はともに必要である。

[^28]: Account
    Managerはオンラインサービスとの同期の仕組みも提供するが、本節では扱っていない。

[^29]: 中間者攻撃については次のページを参照。[*http://www.ipa.go.jp/about/press/20140919\_1.html*](http://www.ipa.go.jp/about/press/20140919_1.html)

[^30]: この危険性については以下の記事で詳しく説明されている

    [*https://www.cigital.com/blog/ineffective-certificate-pinning-implementations/*](https://www.cigital.com/blog/ineffective-certificate-pinning-implementations/)

[^31]: Network Security Configurationの詳細については以下を参照すること
    https://developer.android.com/training/articles/security-config.html

[^32]: HTTP以外の通信方式に対してどのような制御が行われるかについては、以下を参照すること
    https://developer.android.com/reference/android/security/NetworkSecurityPolicy.html\#isCleartextTrafficPermitted()

[^33]: http://www.kddilabs.jp/tech/public-tech/appgen.html

[^34]: 2016年9月1日版執筆時点の情報。後日、修正される可能性がある。