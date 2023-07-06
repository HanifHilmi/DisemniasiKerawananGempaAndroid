package com.hilmihanif.kerawanangempadantsunami.screens.kerawanan

import android.content.res.Configuration
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hilmihanif.kerawanangempadantsunami.R
import com.hilmihanif.kerawanangempadantsunami.ui.theme.KerawananGempaDanTsunamiTheme
import com.hilmihanif.kerawanangempadantsunami.utils.GEMPA_LAYER_INDEX
import com.hilmihanif.kerawanangempadantsunami.utils.GM_LAYER_INDEX
import com.hilmihanif.kerawanangempadantsunami.utils.TSUNAMI_LAYER_INDEX
import com.hilmihanif.kerawanangempadantsunami.viewmodels.MainMapViewModel

@Composable
fun ResultCard(
    modifier: Modifier = Modifier,
    viewModel: MainMapViewModel
){
    val resultCardUiState by viewModel.resultCardUiState.collectAsState()

    viewModel.updateLayerLoadStatus()

    ResultCardContent(
        isLayerLoaded = resultCardUiState.isLayerLoaded ,
        modifier = modifier,
        gempaKRBresult = resultCardUiState.identifiedLayerList.let {
            if (it.isNotEmpty()){
                if (it.size >= GEMPA_LAYER_INDEX){
                    it[GEMPA_LAYER_INDEX-1].getValue("KELAS").toString()
                }else "Tidak Tersedia"
            }else "Loading.."
        },
        gmKRBResult = resultCardUiState.identifiedLayerList.let{
            if(it.isNotEmpty()){
                if (it.size >= GM_LAYER_INDEX){
                    it[GM_LAYER_INDEX-1].getValue("NAMOBJ").toString()
                }else "TIdak Tersedia"
            } else "Loading.."
        },
        tsuKRBResult = resultCardUiState.identifiedLayerList.let{
            if(it.isNotEmpty()){
                if (it.size >= TSUNAMI_LAYER_INDEX){
                    it[TSUNAMI_LAYER_INDEX-1].getValue("").toString()
                }else "TIdak Tersedia"
            } else "Loading.."
        },
        onNewInputButtonClicked = {viewModel.resetInput()}
    )
}



@Composable
fun ResultCardContent(
    modifier: Modifier = Modifier,
    isLayerLoaded:Boolean,
    gempaKRBresult:String,
    gmKRBResult:String,
    tsuKRBResult:String,
    onNewInputButtonClicked:() ->Unit ={},
) {

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(MaterialTheme.colorScheme.background, RoundedCornerShape(12.dp))
            .animateContentSize()

    ) {
        if (isLayerLoaded){
            Text(
                text = "Hasil",
                modifier = Modifier.padding(start = 16.dp, top = 16.dp),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
            Row(
                modifier = Modifier.padding(16.dp)
            ) {
                val imageModifier = Modifier.padding(horizontal = 20.dp)

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(id = R.string.result_gempatitle),
                        textAlign = TextAlign.Center,
                        //minLines= 3,
                        maxLines= 3,
                        style = MaterialTheme.typography.labelSmall,

                    )
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.img_earthquake),
                        contentDescription = "",
                        modifier = imageModifier,
                    )
                    Text(
                        text = gempaKRBresult,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(id = R.string.result_gmtitle),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelSmall,
                        )
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.img_groundmotion),
                        contentDescription = "",
                        modifier = imageModifier,
                    )
                    Text(
                        text = gmKRBResult,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(id = R.string.result_tsutitle),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelSmall,
                        )
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.img_tsunami),
                        contentDescription = "",
                        modifier = imageModifier,
                    )
                    Text(
                        text = tsuKRBResult,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            Row(modifier = Modifier.padding(horizontal = 8.dp)) {
                Button(modifier = Modifier.weight(1f).padding(4.dp),
                    onClick = { /*TODO*/ }
                ) {
                    Text(text = "info Lengkap")
                }
                Button(modifier = Modifier.weight(1f).padding(4.dp),
                    onClick = onNewInputButtonClicked
                ) {
                    Text(text = "Input Baru")
                }
            }

        }else{
            Text(
                text = "Loading layers...",
                modifier = modifier
                    .padding(8.dp),
                style = MaterialTheme.typography.titleSmall
            )
            LinearProgressIndicator(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .padding(bottom = 16.dp)
            )
        }


    }
}
@Preview(name = "Dark Mode",uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "Light Mode")
@Composable
fun PreviewResultCard() {
    KerawananGempaDanTsunamiTheme {
        ResultCardContent(
            isLayerLoaded = true,
            gempaKRBresult = "Test",
            gmKRBResult = "Test",
            tsuKRBResult = "Test"
        )
    }

}