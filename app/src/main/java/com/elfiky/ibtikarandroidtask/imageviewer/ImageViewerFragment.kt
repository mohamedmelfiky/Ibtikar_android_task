package com.elfiky.ibtikarandroidtask.imageviewer

import android.Manifest
import android.app.DownloadManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import coil.load
import com.elfiky.ibtikarandroidtask.R
import com.elfiky.ibtikarandroidtask.databinding.ImageViewerFragmentBinding
import com.elfiky.ibtikarandroidtask.delegates.viewBinding
import com.elfiky.ibtikarandroidtask.receiver.OnDownloadCompleteReceiver
import org.koin.android.ext.android.inject

class ImageViewerFragment : Fragment(R.layout.image_viewer_fragment) {

    private val binding by viewBinding(ImageViewerFragmentBinding::bind)
    private val args: ImageViewerFragmentArgs by navArgs()
    private val downloadManager: DownloadManager by inject()
    private val receiver: OnDownloadCompleteReceiver by inject()
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
        ::onPermissionResult
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.personImg.load(args.originalUrl) { crossfade(true) }
        binding.downloadBtn.setOnClickListener { onDownloadClicked() }
        receiver.observe(this, ::onDownloadComplete)
    }

    private fun onDownloadClicked() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        } else {
            downloadImage()
        }
    }

    private fun onPermissionResult(isGranted: Boolean) {
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

    private fun downloadImage() {
        val request = DownloadManager.Request(args.originalUrl.toUri())
            .setDestinationInExternalPublicDir(
                Environment.DIRECTORY_PICTURES,
                "${args.name.replace(" ", "_")}.jpg"
            )

        downloadManager.enqueue(request)
        Toast.makeText(
            requireContext(),
            getString(R.string.download_started),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun onDownloadComplete() {
        Toast.makeText(
            requireContext(),
            getString(R.string.download_completed),
            Toast.LENGTH_SHORT
        ).show()
    }
}