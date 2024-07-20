//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.project.micss.ui.Counselor
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//
//class CounselorsDetailViewModel : ViewModel() {
//
//    private val _counselorDetail = MutableStateFlow<Counselor?>(null)
//    val counselorDetail: StateFlow<Counselor?> get() = _counselorDetail
//
//    fun fetchCounselorDetail(counselorId: String) {
//        viewModelScope.launch {
//            try {
//                val counselor = getCounselorDetailFromFirestore(counselorId)
//                _counselorDetail.value = counselor
//            } catch (e: Exception) {
//                // Handle error, e.g., log or throw further
//                throw e
//            }
//        }
//    }
//
//    private suspend fun getCounselorDetailFromFirestore(counselorId: String): Counselor {
//        return withContext(Dispatchers.IO) {
//            // Simulated data fetching, replace with your Firestore logic
//            // Example code to fetch from Firestore
//            // val docRef = FirebaseFirestore.getInstance().collection("counselors").document(counselorId)
//            // val snapshot = docRef.get().await()
//            // snapshot.toObject(Counselor::class.java) ?: throw Exception("Counselor not found")
//
//            // Simulated data for testing
//            when (counselorId) {
//                "1" -> Counselor("1", "John Doe", "Psychology", "https://example.com/johndoe.jpg")
//                "2" -> Counselor("2", "Jane Smith", "Family Counseling", "https://example.com/janesmith.jpg")
//                else -> throw Exception("Counselor not found")
//            }
//        }
//    }
//}
