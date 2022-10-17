package com.techflitter.myapplicationgit

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.techflitter.myapplicationgit.adapter.PicSumDataAdapter
import com.techflitter.myapplicationgit.api.ApiClient.createRetrofit
import com.techflitter.myapplicationgit.api.ApiService
import com.techflitter.myapplicationgit.databinding.FragmentFirstBinding
import com.techflitter.myapplicationgit.model.PicSumModelItem
import com.techflitter.myapplicationgit.repository.RepositoryData
import com.techflitter.myapplicationgit.viewmodel.Factory
import com.techflitter.myapplicationgit.viewmodel.PicSumViewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        val api = createRetrofit().create(ApiService::class.java)
        val repositoryData = RepositoryData(api)
        val vm by lazy {
            ViewModelProvider(
                this,
                Factory(repositoryData = repositoryData)
            )[PicSumViewModel::class.java]
        }
        val adapter = PicSumDataAdapter(
            requireActivity(),
            mutableListOf()
        )
        val mLytManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.layoutManager = mLytManager
        binding.recyclerView.adapter = adapter
        val list = ArrayList<PicSumModelItem>()
        binding.progressBar.visibility=View.VISIBLE
        vm.getPicSumAPI(2,20)
        vm.getAPIResponse.observe(viewLifecycleOwner){
            if (it.isNotEmpty()) {
                Log.d("TAG", "onViewCreated : airportViewModel : ${it?.size!!}")
                list.clear()
                list.addAll(it)
                adapter.refreshList(list)
                binding.pullToRefresh.isRefreshing = false
                binding.progressBar.visibility=View.GONE
            }
        }
        binding.pullToRefresh.setOnRefreshListener {
            /*if (!Constants.isOnline(activity)) {
                Constants.msgDialog(
                    requireActivity() as AppCompatActivity,
                    getString(R.string.noInternet)
                )
                binding.pullToRefresh.isRefreshing = false
            } else {*/
                vm.getPicSumAPI(1,20)
            //}
        }
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}