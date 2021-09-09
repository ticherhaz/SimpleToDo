package com.hazman.simpletodo.retrofit

import com.hazman.simpletodo.model.ToDo
import com.hazman.simpletodo.utils.ConstantApi
import retrofit2.Response
import retrofit2.http.*

interface ApiInterface {

    @GET(ConstantApi.LIST)
    suspend fun getTodoList(@Query(ConstantApi.PAGE) page: Int,
                            @Query(ConstantApi.LIMIT) limit: Int = 18): Response<ArrayList<ToDo>>

    @POST(ConstantApi.LIST)
    suspend fun setToDo(@Body toDo: ToDo): Response<ToDo>

    @DELETE(ConstantApi.DETAIL)
    suspend fun deleteToDo(@Path(ConstantApi.PATH_ID) id: String): Response<ToDo>

    @PUT(ConstantApi.DETAIL)
    suspend fun updateToDo(@Path(ConstantApi.PATH_ID) id: String, @Body toDo: ToDo): Response<ToDo>

    @GET(ConstantApi.LIST)
    suspend fun searchTitle(@Query(ConstantApi.TITLE) title: String): Response<ArrayList<ToDo>>

    @GET(ConstantApi.LIST)
    suspend fun filterCompleted(@Query(ConstantApi.COMPLETED) completed: String): Response<ArrayList<ToDo>>

//    @POST(ConstantApi.VERIFICATION_CODE)
//    suspend fun setVerificationCode(@Body verificationCodeParameter: VerificationCodeParameter): Response<BasicResponse>
//
//    @POST(ConstantApi.RESET_PASSWORD)
//    suspend fun setResetPassword(@Body resetPasswordParameter: ResetPasswordParameter): Response<BasicResponse>
//
//    @POST(ConstantApi.LOGOUT)
//    suspend fun setLogout(@Body logoutParameter: LogoutParameter): Response<BasicResponse>
//
//    @POST(ConstantApi.PROFILE_UPLOAD)
//    suspend fun setUpload(@Body uploadParameter: UploadParameter): Response<UploadResponse>
//
//    @DELETE(ConstantApi.DELETE_PROFILE_IMG)
//    suspend fun deleteProfileImage(): Response<BasicResponse>
//
//    @GET(ConstantApi.PROFILE)
//    suspend fun getProfile(): Response<UserResponse>
//
//    @POST(ConstantApi.PROFILE_EDIT)
//    suspend fun setUpdateProfile(@Body updateProfileParameter: UpdateProfileParameter): Response<BasicResponse>
//
//    //TRIPS
//    @GET(ConstantApi.TRIP_LIST_UPCOMING)
//    suspend fun getTripListUpcoming(): Response<ListUpcomingResponse>
//
//    @GET(ConstantApi.TRIP_LIST_HISTORY)
//    suspend fun getTripListHistory(
//        @Query(ConstantApi.DAYS) days: Int?,
//        @Query(ConstantApi.TYPES) type: String?,
//        @Query(ConstantApi.SEARCH) search: String?,
//        @Query(ConstantApi.PAGE) page: Int?
//    ): Response<ListUpcomingResponse>
//
//    @GET(ConstantApi.TRIP_DETAIL)
//    suspend fun getTripDetail(
//        @Path(
//            ConstantApi.PATH_ID,
//            encoded = true
//        ) pathId: String
//    ): Response<TripDetailResponse>
//
//    @POST(ConstantApi.TRIP_DEPART)
//    suspend fun setTripDepart(@Body departParameter: DepartParameter): Response<TripDepartResponse>
//
//    @POST(ConstantApi.TRIP_DELIVER)
//    suspend fun setTripDeliver(@Body deliverParameter: DeliverParameter): Response<TripDeliverResponse>
//
//    @POST(ConstantApi.TRIP_REATTEMPT)
//    suspend fun setTripReattempt(@Body reattemptParameter: ReattemptParameter): Response<TripReattemptResponse>
//
//    @POST(ConstantApi.TRIP_PICKUP)
//    suspend fun setTripPickup(@Body pickupParameter: PickupParameter): Response<TripPickupResponse>
//
//    @POST(ConstantApi.TRIP_PICKUP_FAIL)
//    suspend fun setTripPickupFail(@Body pickupFailParameter: PickupFailParameter): Response<TripPickupFailResponse>
//
//    @POST(ConstantApi.TRIP_ARRIVE)
//    suspend fun setTripArrive(@Body arriveParameter: ArriveParameter): Response<TripTransitArriveResponse>
}