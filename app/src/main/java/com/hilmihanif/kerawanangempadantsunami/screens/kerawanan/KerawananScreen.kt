package com.hilmihanif.kerawanangempadantsunami.screens.kerawanan

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arcgismaps.LoadStatus
import com.arcgismaps.geometry.Point
import com.arcgismaps.mapping.ArcGISMap
import com.arcgismaps.mapping.Viewpoint
import com.arcgismaps.mapping.view.MapView
import com.arcgismaps.tasks.geocode.LocatorTask
import com.hilmihanif.kerawanangempadantsunami.R


@Composable
fun KerawananScreen(
    kerawananViewModel: KerawananViewModel = viewModel()
) {
    val toggleList = stringArrayResource(id = R.array.toggle_list).toList()

    val localcontext = LocalContext.current
    val mapUiState by kerawananViewModel.mapUiState.collectAsState()


    kerawananViewModel.updateMapDesc(mapUiState.map.loadStatus.collectAsState())
    kerawananViewModel.setInitToggleState(toggleList)
    kerawananViewModel.setInputDesc(localcontext)
    kerawananViewModel.updateLocatorLoading()

    KerawananContent(
        map = kerawananViewModel.mapUiState.collectAsState().value.map,
        viewpoint = mapUiState.currentViewPoint,
        mapStatus = mapUiState.map.loadStatus.collectAsState(),
        locatorTask = mapUiState.locatorTask!!,
        mapStatusDesc = mapUiState.currentMapStatusDesc,
        onSingleTap = {point, mapView ->
            kerawananViewModel.setInitMapView(mapView)
            kerawananViewModel.setOnTapPinLocation(point,mapView,toggleList)
        },
        onInputToggleChange= {
            kerawananViewModel.updateToggleState(select = it)
        },
        onProcessButtonClick= {
            kerawananViewModel.setOnProcessButtonClicked(prov = mapUiState.currentPinLocation?.provinsi ?: "")
        },
        isInputProcessNotDone = mapUiState.isInputProcessNotDone,
        viewModel= kerawananViewModel,

    )
}


/**
 * Set Kerawanan MapView to Composable
 */
@Composable
fun KerawananContent(
    map:ArcGISMap,
    viewpoint:Viewpoint,
    mapStatus: State<LoadStatus>,
    mapStatusDesc:String,
    isInputProcessNotDone:Boolean,
    locatorTask: LocatorTask,
    onSingleTap: (Point?,MapView) -> Unit,
    onInputToggleChange:(String)->Unit,
    onProcessButtonClick:()->Unit,
    viewModel: KerawananViewModel
) = Box(
        modifier = Modifier
            .fillMaxSize(),
) {
    MapViewWithCompose(
        arcGISMap = map,
        viewpoint = viewpoint,
        onSingleTap = onSingleTap,
    )
    Row(
        modifier = Modifier.align(Alignment.Center)
    ) {
        Text(text = mapStatusDesc)
        AnimatedVisibility (mapStatus.value != LoadStatus.Loaded) {
            CircularProgressIndicator()
        }

    }
    /*
    when {
        (mapStatus.value == LoadStatus.Loaded) && isInputProcessNotDone -> {
            InputKoordinatCard(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .animateContentSize(),
                viewModel = viewModel,
                onToggleChange = { onInputToggleChange(it) },
                locatorTask = locatorTask,
                onProsesButtonClick = onProcessButtonClick,
            )
        }
        (mapStatus.value == LoadStatus.Loaded) && !isInputProcessNotDone -> {
            ResultCard(
                modifier = Modifier.align(Alignment.BottomCenter),
                viewModel = viewModel,
            )
        }
    }
     */

    AnimatedVisibility(
        visible = mapStatus.value == LoadStatus.Loaded,
        modifier = Modifier.align(Alignment.BottomCenter)
    ) {
        Crossfade(targetState = isInputProcessNotDone) {
            when(it){
                true ->{
                    InputKoordinatCard(
                        modifier = Modifier
                            .animateContentSize(),
                        viewModel = viewModel,
                        onToggleChange = { onInputToggleChange(it) },
                        locatorTask = locatorTask,
                        onProsesButtonClick = onProcessButtonClick,
                    )
                }
                false ->{
                    ResultCard(
                        modifier = Modifier.align(Alignment.BottomCenter),
                        viewModel = viewModel,
                    )
                }
            }
        }
    }

    AnimatedVisibility(visible = mapStatus.value == LoadStatus.Loaded){
        MapControllerScreen(viewModel, isLayerIdentified = !isInputProcessNotDone)
    }
}




