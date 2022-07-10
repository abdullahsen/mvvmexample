package com.asen.mvvmexample

import android.content.res.Resources
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher


fun withPositionInRecyclerView(position: Int, recyclerViewId: Int, childViewId: Int = -1): Matcher<View> =
    object : TypeSafeMatcher<View>() {

        private var resources: Resources? = null
        private var recyclerView: RecyclerView? = null
        override fun describeTo(description: Description?) {
            description?.appendText("with position $position in recyclerView")
            if (resources != null) {
                try {
                    description?.appendText(" ${resources?.getResourceName(recyclerViewId)}")
                } catch (exception: Resources.NotFoundException) {
                    description?.appendText(" (with id $recyclerViewId not found)")
                }
            }
            if (childViewId != -1) {
                description?.appendText(" with child view ")
                try {
                    description?.appendText(" ${resources?.getResourceName(childViewId)}")
                } catch (exception: Resources.NotFoundException) {
                    description?.appendText(" (with id $childViewId not found)")
                }
            }
        }

        override fun matchesSafely(view: View?): Boolean {
            if (view == null) return false
            if (resources == null) {
                resources = view.resources;
            }
            if (recyclerView == null) {
                recyclerView = view.rootView.findViewById(recyclerViewId)
            }
            return if (childViewId != -1) {
                view == recyclerView?.findViewHolderForAdapterPosition(position)?.itemView?.findViewById(childViewId)
            } else {
                view == recyclerView?.findViewHolderForAdapterPosition(position)?.itemView;
            }
        }
    }