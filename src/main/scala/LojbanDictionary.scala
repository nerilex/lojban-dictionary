package iocikun.juj.lojban.dictionary

import _root_.android.app.Activity
import _root_.android.os.Bundle
import _root_.android.content.Intent
import _root_.android.view.View
import _root_.android.view.View.OnClickListener
import _root_.android.view.Window
import _root_.android.view.Menu
import _root_.android.view.MenuItem
import _root_.android.widget.TextView
import _root_.android.widget.EditText
import _root_.android.widget.Button
import _root_.android.widget.LinearLayout
import _root_.android.preference.PreferenceManager
import _root_.android.text.Html

class LojbanDictionary extends Activity with TypedActivity {

	lazy val sp = PreferenceManager getDefaultSharedPreferences this
	lazy val dic = new ReadDictionary(getAssets(), sp)

	lazy val input = findView(TR.input)
	lazy val lojen = findView(TR.lojen)
	lazy val enloj = findView(TR.enloj)
	lazy val rafsi = findView(TR.rafsi)
	lazy val field = findView(TR.field)
	lazy val valsi = findView(TR.valsi)
	lazy val velcki = findView(TR.velcki)

	override def onCreate(bundle: Bundle) {
		super.onCreate(bundle)
		requestWindowFeature(Window.FEATURE_NO_TITLE)
		setContentView(R.layout.main)

		lojen setOnClickListener new OnClickListener() {
			def onClick(v: View) =
				putDef(dic lojToEn input.getText.toString)}

		enloj setOnClickListener new OnClickListener() {
			def onClick(v: View) =
				mkEnLoj(dic enToLoj input.getText.toString)}

		rafsi setOnClickListener new OnClickListener() {
			def onClick(v: View) =
				putDef(dic rafsi input.getText.toString)}
	}

	override def onResume {
		super.onResume
		if (sp.contains("lang")) {
			lojen.setText("loj -> " + sp.getString("lang", ""))
			enloj.setText(sp.getString("lang", "") + " -> loj")
		}
	}

	override def onCreateOptionsMenu(menu: Menu): Boolean = {
		menu.add(Menu.NONE, 0, 0, "Select Lang")
		return super.onCreateOptionsMenu(menu)
	}

	override def onOptionsItemSelected(item: MenuItem): Boolean = {
		item.getItemId() match {
		case 0 =>
			val intent = new Intent(this, classOf[Preference])
			startActivity(intent)
		}
		return true
	}

	def putDef(result: (String, String, List[String])) {
		valsi.setText(result._1)
		velcki.setText(Html.fromHtml(result._2))
		field.removeAllViews
		field.addView(valsi)
		field.addView(velcki)
		mkLinks(result._3)
	}

	def mkLinks(list: List[String]) {
		for (v <- list) {
			val tv = new TextView(this)
			tv.setTextSize(30)
			tv.setText(v)
			tv.setClickable(true)
			tv.setOnClickListener(new View.OnClickListener() {
				def onClick(_v: View) {
					val result = dic.lojToEn(v)
					putDef(result)
				}
			})
			field.addView(tv)
		}
	}

	def mkEnLoj(list: List[(String, String)]) {
		field.removeAllViews
		for (result <- list) {
			val tv1 = new TextView(this)
			val tv2 = new TextView(this)
			tv1.setTextSize(30)
			tv1.setText(result._1)
			tv1.setOnClickListener(new View.OnClickListener() {
				def onClick(v: View) {
					val result2 = dic.lojToEn(result._1)
					putDef(result2)
				}
			})
			tv1.setClickable(true)
			tv2.setText(Html.fromHtml(result._2))
			field.addView(tv1)
			field.addView(tv2)
		}
	}
}
