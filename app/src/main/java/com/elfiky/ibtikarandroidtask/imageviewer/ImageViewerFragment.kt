package com.elfiky.ibtikarandroidtask.imageviewer

import android.Manifest
import android.app.DownloadManager
import android.content.Context
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import coil.load
import com.elfiky.ibtikarandroidtask.R
import com.elfiky.ibtikarandroidtask.databinding.ImageViewerFragmentBinding
import com.elfiky.ibtikarandroidtask.receiver.OnDownloadCompleteReceiver

class ImageViewerFragment : Fragment() {

    private var _binding: ImageViewerFragmentBinding? = null
    private val binding get() = _binding!!
    private val args: ImageViewerFragmentArgs by navArgs()
    private val downloadManager by lazy {
        requireActivity().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    }
    private val receiver = OnDownloadCompleteReceiver(::onDownloadComplete)
    private var downloadedId: Long? = null

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                downloadImage()
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.permission_denied),
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ImageViewerFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.personImg.load(args.originalUrl) {
            crossfade(true)
        }

        binding.downloadBtn.setOnClickListener {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            } else {
                downloadImage()
            }
        }

        val filter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        requireContext().registerReceiver(receiver, filter)
    }

    private fun downloadImage() {
        val request = DownloadManager.Request(args.originalUrl.toUri())
            .setDestinationInExternalPublicDir(
                Environment.DIRECTORY_PICTURES,
                "${args.name.replace(" ", "_")}.jpg"
            )

        downloadedId = downloadManager.enqueue(request)
        Toast.makeText(requireContext(), getString(R.string.download_started), Toast.LENGTH_SHORT)
            .show()
    }

    private fun onDownloadComplete(id: Long) {
        if (id != downloadedId) return
        Toast.makeText(requireContext(), getString(R.string.download_completed), Toast.LENGTH_SHORT)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        requireContext().unregisterReceiver(receiver)
    }
}