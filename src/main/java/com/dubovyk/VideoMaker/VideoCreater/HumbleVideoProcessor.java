package com.dubovyk.VideoMaker.VideoCreater;

import com.dubovyk.VideoMaker.ImageProcessor.ImageResize;
import com.dubovyk.VideoMaker.Models.Data.SingleSlide;
import com.dubovyk.VideoMaker.Models.Data.VideoData;
import io.humble.video.*;
import io.humble.video.awt.MediaPictureConverter;
import io.humble.video.awt.MediaPictureConverterFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.prefs.Preferences;

/**
 * @author Sergey Dubovyk
 * @version 1.0
 */
public class HumbleVideoProcessor extends Thread{
    private VideoData videoData;
    private Thread t;

    public HumbleVideoProcessor(VideoData videoData){
        this.videoData = videoData;
    }

    public void run(){
        processVideo();
    }

    public void start(){
        if(t == null){
            t = new Thread(this, "VideoProcessor");
            t.start();
        }
    }

    public void processVideo(){
        try {
            final Rational framerate = Rational.make(1, 25);
            final Muxer muxer = Muxer.make(videoData.getFilePath(), null, "mp4");
            final MuxerFormat format = muxer.getFormat();
            final Codec codec;

            codec = Codec.findEncodingCodec(format.getDefaultVideoCodecId());

            Encoder encoder = Encoder.make(codec);


            encoder.setWidth(videoData.getFrameWidth());
            encoder.setHeight(videoData.getFrameHeight());
            final PixelFormat.Type pixelformat = PixelFormat.Type.PIX_FMT_YUV420P;
            encoder.setPixelFormat(pixelformat);
            encoder.setTimeBase(framerate);

            if (format.getFlag(MuxerFormat.Flag.GLOBAL_HEADER))
                encoder.setFlag(Encoder.Flag.FLAG_GLOBAL_HEADER, true);

            encoder.open(null, null);

            muxer.addNewStream(encoder);

            muxer.open(null, null);

            final MediaPicture picture = MediaPicture.make(
                    encoder.getWidth(),
                    encoder.getHeight(),
                    pixelformat);
            picture.setTimeBase(framerate);

            final MediaPacket packet = MediaPacket.make();
            long current = 0;

            if (videoData.getIntroPath() != null){
                current = appendVideo(videoData.getIntroPath(), encoder, muxer, picture, packet, current);
            }
            for(SingleSlide slide: videoData.getVideoSlides()){
                BufferedImage image = ImageIO.read(new File(slide.getFilePath()));
                for(int i = 0 ; i < 25 * 10; i++){
                    current++;
                    encodeImage(encoder, muxer, picture, packet, image, current);
                }
            }
            if (videoData.getOutroPath() != null){
                current = appendVideo(videoData.getOutroPath(), encoder, muxer, picture, packet, current);
            }
            do {
                encoder.encode(packet, null);
                if (packet.isComplete())
                    muxer.write(packet,  false);
            } while (packet.isComplete());

            muxer.close();
        } catch (Exception ex){
            ex.printStackTrace();
        }
        System.out.println("Audio starts");
        AudioMerger merger = new AudioMerger(videoData);
        merger.addAudioAllLength();
    }

    public long appendVideo(String filename, Encoder outEncoder, Muxer outMuxer, MediaPicture outPicture, MediaPacket outPacket, long startPos){
        try {
            Demuxer demuxer = Demuxer.make();

            try {
                demuxer.open(filename, null, false, true, null, null);
            } catch (Exception ex){
                ex.printStackTrace();
            }

            int numStreams = demuxer.getNumStreams();

            int videoStreamId = -1;
            Decoder videoDecoder = null;
            for (int i = 0; i < numStreams; i++) {
                final DemuxerStream stream = demuxer.getStream(i);
                final Decoder decoder = stream.getDecoder();
                if (decoder != null && decoder.getCodecType() == MediaDescriptor.Type.MEDIA_VIDEO) {
                    videoStreamId = i;
                    videoDecoder = decoder;
                    break;
                }
            }

            if (videoStreamId == -1)
                throw new RuntimeException("could not find video stream in container: " + filename);

            videoDecoder.open(null, null);

            final MediaPicture picture = MediaPicture.make(
                    videoDecoder.getWidth(),
                    videoDecoder.getHeight(),
                    videoDecoder.getPixelFormat());

            final MediaPictureConverter converter =
                    MediaPictureConverterFactory.createConverter(
                            MediaPictureConverterFactory.HUMBLE_BGR_24,
                            picture);
            BufferedImage image = null;

            final MediaPacket packet = MediaPacket.make();
            while (demuxer.read(packet) >= 0) {
                if (packet.getStreamIndex() == videoStreamId) {
                    int offset = 0;
                    int bytesRead = 0;
                    do {
                        bytesRead += videoDecoder.decode(picture, packet, offset);
                        if (picture.isComplete()) {
                            image = converter.toImage(image, picture);
                            startPos++;
                            encodeImage(outEncoder, outMuxer, outPicture, outPacket, ImageResize.resizeImage(image, videoData.getFrameWidth(), videoData.getFrameHeight()), startPos);
                        }
                        offset += bytesRead;
                    } while (offset < packet.getSize());
                }
            }

            do {
                videoDecoder.decode(picture, null, 0);
                if (picture.isComplete()) {
                    startPos++;
                    image = converter.toImage(image, picture);
                    encodeImage(outEncoder, outMuxer, outPicture, outPacket, ImageResize.resizeImage(image, videoData.getFrameWidth(), videoData.getFrameHeight()), startPos);
                }
            } while (picture.isComplete());

            demuxer.close();
            } catch (Exception ex){
                ex.printStackTrace();
        }
        return startPos;
    }

    private void encodeImage(Encoder encoder, Muxer muxer, MediaPicture picture, MediaPacket packet, BufferedImage image, long i){
        MediaPictureConverter converter = null;
        image = ImageResize.resizeImage(image, videoData.getFrameWidth(), videoData.getFrameHeight());
        image = convertToType(image, BufferedImage.TYPE_3BYTE_BGR);

        converter = MediaPictureConverterFactory.createConverter(image, picture);
        converter.toPicture(picture, image, i);

        do {
            encoder.encode(packet, picture);
            if (packet.isComplete())
                muxer.write(packet, false);
        } while (packet.isComplete());
    }

    public BufferedImage convertToType(BufferedImage sourceImage,
                                              int targetType)
    {
        BufferedImage image;

        // if the source image is already the target type, return the source image

        if (sourceImage.getType() == targetType)
            image = sourceImage;

            // otherwise create a new image of the target type and draw the new
            // image

        else
        {
            image = new BufferedImage(sourceImage.getWidth(),
                    sourceImage.getHeight(), targetType);
            image.getGraphics().drawImage(sourceImage, 0, 0, null);
        }

        return image;
    }
}