package com.jingom.simplealarmmanager.presentation.timealarm.list

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeableState
import androidx.compose.material.TopAppBar
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import com.jingom.simplealarmmanager.R
import com.jingom.simplealarmmanager.ui.theme.SimpleAlarmManagerTheme
import kotlin.math.max
import kotlin.math.roundToInt

enum class BottomSheetVisibilityState {
	HIDE,
	COLLAPSED,
	EXPANDED
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TopBarIntegrationBottomSheetScaffold(
	modifier: Modifier = Modifier,
	topBar: @Composable (() -> Unit)? = null,
	sheetHeader: @Composable () -> Unit = { DefaultBottomSheetHeader() },
	sheetContent: @Composable () -> Unit,
	sheetPickHeight: Dp = 56.dp,
	sheetState: BottomSheetVisibilityState,
	containerColor: Color = MaterialTheme.colorScheme.surface,
	contentColor: Color = contentColorFor(containerColor),
	content: @Composable (PaddingValues) -> Unit,
) {
	val swipeableState = rememberSwipeableState(initialValue = sheetState)

	TopBarIntegrationBottomSheetLayout(
		modifier = modifier,
		sheetPeekHeight = sheetPickHeight,
		sheetOffset = { swipeableState.offset.value },
		containerColor = containerColor,
		contentColor = contentColor,
		topBar = topBar,
		body = content,
		sheetHeaderOnlyForMeasure = sheetHeader,
		sheetContentOnlyForMeasure = sheetContent,
		bottomSheet = { layoutHeight, topBarHeight, sheetHeaderHeight, sheetContentHeight ->
			val scrollState = rememberScrollState()
			val sheetPickHeightInPx = with(LocalDensity.current) {
				sheetPickHeight.toPx()
			}
			Box(
				Modifier
					.bottomSheetSwipeable(
						swipeableState = swipeableState,
						layoutHeight = layoutHeight.toFloat(),
						topBarHeight = topBarHeight.toFloat(),
						sheetHeaderHeight = sheetHeaderHeight.toFloat(),
						sheetContentHeight = sheetContentHeight.toFloat(),
						peekHeight = sheetPickHeightInPx
					)
					.bottomSheetNestedScroll(swipeableState, scrollState)
			) {
				Column(Modifier.fillMaxSize()) {
					sheetHeader()
					Surface(
						color = containerColor,
						contentColor = contentColor,
						modifier = Modifier
							.fillMaxSize()
							.verticalScroll(
								state = scrollState,
								flingBehavior = ScrollableDefaults.flingBehavior()
							)
					) {
						sheetContent()
					}
				}
			}
		}
	)
}

@OptIn(ExperimentalMaterialApi::class)
private fun Modifier.bottomSheetSwipeable(
	swipeableState: SwipeableState<BottomSheetVisibilityState>,
	topBarHeight: Float,
	sheetHeaderHeight: Float,
	layoutHeight: Float,
	sheetContentHeight: Float,
	peekHeight: Float
): Modifier {
	return this.swipeable(
		state = swipeableState,
		orientation = Orientation.Vertical,
		anchors = mapOf(
			topBarHeight - sheetHeaderHeight to BottomSheetVisibilityState.EXPANDED,
			layoutHeight - max(peekHeight, sheetHeaderHeight + sheetContentHeight) to BottomSheetVisibilityState.COLLAPSED,
			layoutHeight to BottomSheetVisibilityState.HIDE,
		)
	)
}

@OptIn(ExperimentalMaterialApi::class)
private fun Modifier.bottomSheetNestedScroll(
	swipeableState: SwipeableState<BottomSheetVisibilityState>,
	scrollState: ScrollState
): Modifier {
	return nestedScroll(
		object : NestedScrollConnection {
			override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
				val delta = available.y
				return if (delta < 0) {
					Offset(x = 0f, y = swipeableState.performDrag(delta))
				} else {
					Offset.Zero
				}
			}

			override fun onPostScroll(consumed: Offset, available: Offset, source: NestedScrollSource): Offset {
				val delta = available.y
				return Offset(x = 0f, y = swipeableState.performDrag(delta))
			}

			override suspend fun onPreFling(available: Velocity): Velocity {
				return if (available.y < 0 && scrollState.value == 0) {
					swipeableState.performFling(available.y)
					available
				} else {
					Velocity.Zero
				}
			}

			override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
				swipeableState.performFling(available.y)
				return super.onPostFling(consumed, available)
			}
		}
	)
}

@Composable
private fun TopBarIntegrationBottomSheetLayout(
	modifier: Modifier,
	sheetPeekHeight: Dp,
	sheetOffset: () -> Float,
	containerColor: Color,
	contentColor: Color,
	topBar: @Composable (() -> Unit)?,
	body: @Composable (innerPadding: PaddingValues) -> Unit,
	bottomSheet: @Composable (layoutHeight: Int, topBarHeight: Int, sheetHeaderHeight: Int, sheetContentHeight: Int) -> Unit,
	sheetHeaderOnlyForMeasure: @Composable () -> Unit = { DefaultBottomSheetHeader() },
	sheetContentOnlyForMeasure: @Composable () -> Unit
) {
	SubcomposeLayout { constraints ->
		val layoutWidth = constraints.maxWidth
		val layoutHeight = constraints.maxHeight
		val looseConstraints = constraints.copy(minWidth = 0, minHeight = 0)

		val topBarPlaceable = topBar?.let {
			subcompose(TopBarIntegrationBottomSheetLayoutSlot.TopBar) { topBar() }[0]
				.measure(looseConstraints)
		}
		val topBarHeight = topBarPlaceable?.height ?: 0

		// 측정만 하고 배치는 하지 않음.
		// 바텀 시트 헤더가 Top Bar 에 가려지도록 swipe offset 을 조절하기 위해 측정한다.
		val sheetHeaderPlaceable = subcompose(TopBarIntegrationBottomSheetLayoutSlot.SheetHeader) {
			sheetHeaderOnlyForMeasure()
		}[0].measure(looseConstraints)

		val sheetContentPlaceable = subcompose(Unit) {
			sheetContentOnlyForMeasure()
		}[0].measure(looseConstraints)

		val sheetPlaceable = subcompose(TopBarIntegrationBottomSheetLayoutSlot.Sheet) {
			bottomSheet(
				layoutHeight = layoutHeight,
				topBarHeight = topBarHeight,
				sheetHeaderHeight = sheetHeaderPlaceable.height,
				sheetContentHeight = sheetContentPlaceable.height
			)
		}[0].measure(looseConstraints.copy(maxHeight = layoutHeight - topBarHeight + sheetHeaderPlaceable.height))
		val sheetOffsetY = sheetOffset().roundToInt()
		val sheetOffsetX = Integer.max(0, (layoutWidth - sheetPlaceable.width) / 2)

		val bodyConstraints = looseConstraints.copy(maxHeight = layoutHeight - topBarHeight)
		val bodyPlaceable = subcompose(TopBarIntegrationBottomSheetLayoutSlot.Body) {
			Surface(
				modifier = modifier,
				color = containerColor,
				contentColor = contentColor,
			) { body(PaddingValues(bottom = sheetPeekHeight)) }
		}[0].measure(bodyConstraints)

		layout(layoutWidth, layoutHeight) {
			bodyPlaceable.placeRelative(0, topBarHeight)
			sheetPlaceable.placeRelative(sheetOffsetX, sheetOffsetY)
			topBarPlaceable?.placeRelative(0, 0)
		}
	}
}

private enum class TopBarIntegrationBottomSheetLayoutSlot {
	TopBar,
	Body,
	SheetHeader,
	Sheet
}

@Preview
@Composable
private fun TopBarIntegrationBottomSheetScaffoldExpandedPreview() {
	SimpleAlarmManagerTheme {
		TopBarIntegrationBottomSheetScaffold(
			topBar = {
				TopAppBar(
					modifier = Modifier
						.fillMaxWidth()
						.height(58.dp)
				) {

				}
			},
			sheetContent = {
				Box(
					modifier = Modifier
						.fillMaxWidth()
						.background(Color.White)
				) {
					Text(text = "Bottom Sheet Content")
				}
			},
			sheetState = BottomSheetVisibilityState.EXPANDED
		) {
			Text(text = "Body")
		}
	}
}

@Preview
@Composable
private fun TopBarIntegrationBottomSheetScaffoldCollapsedPreview() {
	SimpleAlarmManagerTheme {
		TopBarIntegrationBottomSheetScaffold(
			topBar = {
				TopAppBar(
					modifier = Modifier
						.fillMaxWidth()
						.height(58.dp)
				) {

				}
			},
			sheetContent = {
				Column(
					modifier = Modifier
						.fillMaxWidth()
						.background(Color.White)
				) {
					Text(text = "test")
					Text(text = "test")
					Text(text = "test")
					Text(text = "test")
					Text(text = "test")
					Text(text = "test")
				}
			},
			sheetState = BottomSheetVisibilityState.COLLAPSED
		) {
			Text(text = "Body")
		}
	}
}

@Preview
@Composable
private fun TopBarIntegrationBottomSheetScaffoldHidePreview() {
	SimpleAlarmManagerTheme {
		TopBarIntegrationBottomSheetScaffold(
			topBar = {
				TopAppBar(
					modifier = Modifier
						.fillMaxWidth()
						.height(58.dp)
				) {

				}
			},
			sheetContent = {
				Box(
					modifier = Modifier
						.fillMaxWidth()
						.height(100.dp)
						.background(Color.White)
				) {
					Text(text = "Bottom Sheet Content")
				}
			},
			sheetState = BottomSheetVisibilityState.HIDE
		) {
			Text(text = "Body")
		}
	}
}

@Composable
private fun DefaultBottomSheetHeader() {
	Box(
		modifier = Modifier
			.fillMaxWidth()
			.clip(
				shape = RoundedCornerShape(
					topStart = dimensionResource(R.dimen.bottom_sheet_round_corner_radius_20),
					topEnd = dimensionResource(R.dimen.bottom_sheet_round_corner_radius_20)
				)
			)
			.background(
				color = Color.White,
				shape = RoundedCornerShape(
					topStart = dimensionResource(R.dimen.bottom_sheet_round_corner_radius_20),
					topEnd = dimensionResource(R.dimen.bottom_sheet_round_corner_radius_20)
				)
			)
	) {
		DefaultDragHandle(
			modifier = Modifier
				.padding(
					top = dimensionResource(R.dimen.drag_handle_margin_top),
					bottom = dimensionResource(R.dimen.drag_handle_margin_bottom)
				)
				.align(Alignment.TopCenter)
		)
	}
}

@Composable
private fun DefaultDragHandle(
	modifier: Modifier = Modifier
) {
	Divider(
		modifier = modifier
			.width(dimensionResource(R.dimen.drag_handle_width))
			.height(dimensionResource(R.dimen.drag_handle_height))
			.background(
				color = colorResource(R.color.drag_handle_color),
				shape = RoundedCornerShape(dimensionResource(R.dimen.drag_handle_corner_radius))
			)
	)
}

@Preview
@Composable
private fun DefaultBottomSheetHeaderPreview() {
	SimpleAlarmManagerTheme {
		Column {
			Spacer(modifier = Modifier.height(100.dp))
			DefaultBottomSheetHeader()
		}
	}
}