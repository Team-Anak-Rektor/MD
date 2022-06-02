package com.bintang.bangkitcapstoneproject.model

import com.google.gson.annotations.SerializedName

data class NearbySearchResponse(

	@field:SerializedName("next_page_token")
	val nextPageToken: String,

	@field:SerializedName("html_attributions")
	val htmlAttributions: List<Any>,

	@field:SerializedName("results")
	val results: List<NearbySearchResult>,

	@field:SerializedName("status")
	val status: String
)

data class NearbySearchResult(

	@field:SerializedName("types")
	val types: List<String>,

	@field:SerializedName("business_status")
	val businessStatus: String,

	@field:SerializedName("icon")
	val icon: String,

	@field:SerializedName("rating")
	val rating: Double,

	@field:SerializedName("icon_background_color")
	val iconBackgroundColor: String,

	@field:SerializedName("photos")
	val photos: List<PhotosItem>,

	@field:SerializedName("reference")
	val reference: String,

	@field:SerializedName("user_ratings_total")
	val userRatingsTotal: Int,

	@field:SerializedName("scope")
	val scope: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("geometry")
	val geometry: Geometry,

	@field:SerializedName("icon_mask_base_uri")
	val iconMaskBaseUri: String,

	@field:SerializedName("vicinity")
	val vicinity: String,

	@field:SerializedName("plus_code")
	val plusCode: PlusCode,

	@field:SerializedName("place_id")
	val placeId: String,

	@field:SerializedName("opening_hours")
	val openingHours: OpeningHours,

	@field:SerializedName("permanently_closed")
	val permanentlyClosed: Boolean,

	@field:SerializedName("price_level")
	val priceLevel: Int
)
