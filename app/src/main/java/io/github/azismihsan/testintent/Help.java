ilder} instance.
         * @throws NullPointerException thrown when a null focus listener is used.
         */
        public @NonNull Builder setOnAudioFocusChangeListener(
                @NonNull OnAudioFocusChangeListener listener) {
            if (listener == null) {
                throw new NullPointerException("Illegal null focus listener");
            }
            mFocusListener = listener;
            mListenerHandler = null;
            return this;
        }

        /**
         * @hide
         * Internal listener setter, no null checks on listener nor handler
         * @param listener
         * @param handler
         * @return this {@code Builder} instance.
         */
        @NonNull Builder setOnAudioFocusChangeListenerInt(
                OnAudioFocusChangeListener listener, Handler handler) {
            mFocusListener = listener;
            mListenerHandler = handler;
            return this;
        }

        /**
         * Sets the listener called when audio focus changes after being requested with
         *   {@link AudioManager#requestAudioFocus(AudioFocusRequest)}, and until being abandoned
         *   with {@link AudioManager#abandonAudioFocusRequest(AudioFocusRequest)}.
         *   Note that only focus changes (gains and losses) affecting the focus owner are reported,
         *   not gains and losses of other focus requesters in the system.
         * @param listener the listener receiving the focus change notifications.
         * @param handler the {@link Handler} for the thread on which to execute
         *   the notifications.
         * @return this {@code Builder} instance.
         * @throws NullPointerException thrown when a null focus listener or handler is used.
         */
        public @NonNull Builder setOnAudioFocusChangeListener(
                @NonNull OnAudioFocusChangeListener listener, @NonNull Handler handler) {
            if (listener == null || handler == null) {
                throw new NullPointerException("Illegal null focus listener or handler");
            }
            mFocusListener = listener;
            mListenerHandler = handler;
            return this;
        }

        /**
         * Sets the {@link AudioAttributes} to be associated with the focus request, and which
         * describe the use case for which focus is requested.
         * As the focus requests typically precede audio playback, this information is used on
         * certain platforms to declare the subsequent playback use case. It is therefore good
         * practice to use in this method the same {@code AudioAttributes} as used for
         * playback, see for example {@link MediaPlayer#setAudioAttributes(AudioAttributes)} in
         * {@code MediaPlayer} or {@link AudioTrack.Builder#setAudioAttributes(AudioAttributes)}
         * in {@code AudioTrack}.
         * @param attributes the {@link AudioAttributes} for the focus request.
         * @return this {@code Builder} instance.
         * @throws NullPointerException thrown when using null for the attributes.
         */
        public @NonNull Builder setAudioAttributes(@NonNull AudioAttributes attributes) {
            if (attributes == null) {
                throw new NullPointerException("Illegal null AudioAttributes");
            }
            mAttr = attributes;
            return this;
        }

        /**
         * Declare the intended behavior of the application with regards to audio ducking.
         * See more details in the {@link AudioFocusRequest} class documentation.
         * @param pauseOnDuck use {@code true} if the application intends to pause audio playback
         *    when losing focus with {@link AudioManager#AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK}.
         *    If {@code true}, note that you must also set a focus listener to receive such an
         *    event, with
         *    {@link #setOnAudioFocusChangeListener(OnAudioFocusChangeListener, Handler)}.
         * @return this {@code Builder} instance.
         */
        public @NonNull Builder setWillPauseWhenDucked(boolean pauseOnDuck) {
            mPausesOnDuck = pauseOnDuck;
            return this;
        }

        /**
         * Marks this focus request as compatible with delayed focus.
         * See more details about delayed focus in the {@link AudioFocusRequest} class
         * documentation.
         * @param acceptsDelayedFocusGain use {@code true} if the application supports delayed
         *    focus. If {@code true}, note that you must also set a focus listener to be notified
         *    of delayed focus gain, with
         *    {@link #setOnAudioFocusChangeListener(OnAudioFocusChangeListener, Handler)}.
         * @return this {@code Builder} instance
         */
        public @NonNull Builder setAcceptsDelayedFocusGain(boolean acceptsDelayedFocusGain) {
            mDelayedFocus = acceptsDelayedFocusGain;
            return this;
        }

        /**
         * @hide
         * Marks this focus request as locking audio focus so granting is temporarily disabled.
         * This feature can only be used by owners of a registered
         * {@link android.media.audiopolicy.AudioPolicy} in
         * {@link AudioManager#requestAudioFocus(AudioFocusRequest, android.media.audiopolicy.AudioPolicy)}.
         * Setting to false is the same as the default behavior.
         * @param focusLocked true when locking focus
         * @return this {@code Builder} instance
         */
        @SystemApi
        public @NonNull Builder setLocksFocus(boolean focusLocked) {
            mFocusLocked = focusLocked;
            return this;
        }

        /**
         * Builds a new {@code AudioFocusRequest} instance combining all the information gathered
         * by this {@code Builder}'s configuration methods.
         * @return the {@code AudioFocusRequest} instance qualified by all the properties set
         *   on this {@code Builder}.
         * @throws IllegalStateException thrown when attempting to build a focus request that is set
         *    to accept delayed focus, or to pause on duck, but no focus change listener was set.
         */
        public AudioFocusRequest build() {
            if ((mDelayedFocus || mPausesOnDuck) && (mFocusListener == null)) {
                throw new IllegalStateException(
                        "Can't use delayed focus or pause on duck without a listener");
            }
            final int flags = 0
                    | (mDelayedFocus ? AudioManager.AUDIOFOCUS_FLAG_DELAY_OK : 0)
                    | (mPausesOnDuck ? AudioManager.AUDIOFOCUS_FLAG_PAUSES_ON_DUCKABLE_LOSS : 0)
                    | (mFocusLocked  ? AudioManager.AUDIOFOCUS_FLAG_LOCK : 0);
            return new AudioFocusRequest(mFocusListener, mListenerHandler,
                    mAttr, mFocusGain, flags);
        }
    }
}
                                                                                                                                                                                                                                                                                                                                           