package com.zhei.lawy
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel

fun main () {
}

class MyViewRepo (private val context: Context) {}
class ViewModelRepo(private val myRepo: MyViewRepo): ViewModel() {}


@Composable fun Initializable() {
    val viewmodelrepo: ViewModelRepo = viewModel(factory = FactoryMyViewRepo(LocalContext.current))
}

/**
 * Se encarga de retornar un tipo de ViewModel()
 * */
class FactoryMyViewRepo(private val context: Context): ViewModelProvider.Factory
{
    private val repo by lazy { MyViewRepo(context) }

    override fun <T : ViewModel> create(modelClass: Class<T>): T = when {
        modelClass.isAssignableFrom(ViewModelRepo::class.java) -> {
            ViewModelRepo(repo) as T
        }
        else -> throw IllegalArgumentException("Clase ViewModel desconocida: ${modelClass.name}")
    }

}