/* Copyright 2014 Esri

All rights reserved under the copyright laws of the United States
and applicable international laws, treaties, and conventions.

You may freely redistribute and use this sample code, with or
without modification, provided you include the original copyright
notice and use restrictions.

See the use restrictions.*/
package com.esri.client.toolkit.sliders;

import java.awt.Component;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.Timer;
import javax.swing.ToolTipManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.esri.client.toolkit.sliders.RangeSlider.RangeMode;
import com.esri.core.map.TimeExtent;
import com.esri.core.map.TimeInfo;
import com.esri.core.map.TimeOptions;
import com.esri.core.map.TimeOptions.Units;
import com.esri.map.TimeAwareLayer;

/**
 * This class implements a slider control that can be used to set date ranges to
 * filter time aware layers. The date range is set using two sliders which will
 * update any associated layers that implement {@link TimeAwareLayer}. The
 * control also has a playback button that will animate the layers by moving the
 * currently set time interval. The playback rate is set with
 * {@link #setPlaybackRate(int)}.The control also has buttons that can be used
 * to step the time interval backwards and forwards.
 * <p/>
 * The control is initialised by calling
 * {@link #setTimeExtent(TimeExtent, int, Units)} and passing it the full time
 * extent for the data to be displayed, the interval between ticks on the slider
 * and the time unit that each tick uses.
 * <p/>
 * The control has labels that show the upper and lower limits and also the
 * current time interval. Each of these can be turned on and off. The format of
 * the dates and times displayed can set with {@link #setDateFormat(DateFormat)}
 * or left at a default based on the unit passed to <code>setTimeExtent</code>.
 * For slider tick intervals of century, decade or year, just the year is
 * displayed. For months, weeks or days, the full date is displayed. For hours,
 * minutes, seconds or milliseconds, the time is displayed.
 * <p/>
 * The layers that will be filtered by this control can be added with
 * {@link #addLayer(TimeAwareLayer)} and removed with
 * {@link #removeLayer(TimeAwareLayer)}. Layers currently added to the control
 * will be updated each time either of the thumbs on the control are moved.
 * @deprecated From 10.2.3, use {@link com.esri.toolkit.sliders.JTimeSlider} instead.
 */
@Deprecated
public class JTimeSlider extends JPanel {
    public enum TimeMode {
        CumulativeFromStart, TimeExtent, TimeInstant
    }

    private static final long serialVersionUID = 1L;
    private JLabel _lblTitle;
    private JLabel _lblLowerValue;
    private JLabel _lblUpperValue;
    private TimeExtent _timeExtent;
    private RangeSlider _rangeSlider;
    private JLabel _lblCurrentRange;
    // unit of date as specified in java.util.Calendar field constants
    private int _dateField;
    private DateFormat _dateFormatter;
    private boolean _userSetDateFormat;
    private List<TimeAwareLayer> _timeLayers = new ArrayList<TimeAwareLayer>();
    private final Action playAction = new PlayAction();
    private JToggleButton _btnPlay;
    private JButton _btnStepBack;
    private JButton _btnStepForward;
    private Timer _playbackTimer;
    private int _playbackRate = 1000;
    private int _tickSize;
    private TimeOptions.Units _tickUnit;
    private TimeMode _timeMode = TimeMode.TimeExtent;
    private final Action stepBackAction = new StepBackAction();
    private final Action stepForwardAction = new StepForwardAction();
    private int _unitBasedStepSizeHint = 0;

    /**
     * Create a new time slider instance
     */
    public JTimeSlider() {
        initGui();
    }

    /**
     * Gets the title.
     *
     * @return the title
     */
    public String getTitle() {
        return _lblTitle.getText();
    }

    /**
     * Sets the title.
     *
     * @param title
     *            the new title
     */
    public void setTitle(String title) {
        _lblTitle.setText(title);
    }

    /**
     * Sets whether or not the upper and lower limit labels are shown.
     *
     * @param visible
     *            true to turn the labels on, false to turn them off
     */
    public void setLimitsVisible(boolean visible) {
        _lblLowerValue.setVisible(visible);
        _lblUpperValue.setVisible(visible);
    }

    /**
     * Sets whether or not the current range is visible.
     *
     * @param visible
     *            true to show the current range, false to hide it
     */
    public void setCurrentRangeVisible(boolean visible) {
        _lblCurrentRange.setVisible(visible);
    }

    /**
     * Gets the date format used to format the labels.
     *
     * @return the date format currently being used
     */
    public DateFormat getDateFormat() {
        return _dateFormatter;
    }

    /**
     * Sets the date format used to format the labels.
     *
     * @param dateFormat
     *            the new date format
     */
    public void setDateFormat(DateFormat dateFormat) {
        _userSetDateFormat = true;
        _dateFormatter = dateFormat;
    }

    /**
     * Gets the current time mode of the control.
     *
     * @return the current time mode
     */
    public TimeMode getTimeMode() {
        return _timeMode;
    }

    /**
     * Sets the current time mode of the control. The following values can be
     * set: <li>CumulativeFromStart - represents a fixed start date that does
     * not change and an end date that can change. <li>TimeExtent - represents a
     * start date that can change and an end date that can also change. <li>
     * TimeInstant - represents a start date and end date that are always the
     * same date.
     *
     * @param timeMode
     *            the current time mode
     */
    public void setTimeMode(TimeMode timeMode) {
        _timeMode = timeMode;

        if (_rangeSlider != null) {
            _rangeSlider.setRangeMode(timeModeToRangeMode(_timeMode));
        }
    }

    /**
     * Gets the full time extent.
     *
     * @return the time extent
     */
    public TimeExtent getTimeExtent() {
        return _timeExtent;
    }

    /**
     * Sets the full time extent. This defines the upper and lower limits of the
     * control along with the size of a single step of the slider and the units
     * for that step.
     * <p/>
     * Note that the time extent includes of the start date and excludes the end
     * date. For example, to get the year range 1970 to 1980, the time extent
     * would need to be set from 1970/0/0 to 1980/1/1.
     *
     * @param timeExtent
     *            the full time extent
     * @param tickSize
     *            the tick size
     * @param tickUnit
     *            the tick unit
     */
    public void setTimeExtent(TimeExtent timeExtent, int tickSize,
            TimeOptions.Units tickUnit) {
        // step size is based on unit. so reset existing value (could have been
        // calculated with a different unit)
        _unitBasedStepSizeHint = 0;
        _timeExtent = timeExtent;

        if (tickUnit == TimeOptions.Units.Unknown) {
            this._tickUnit = determineTickUnit();
        } else {
            this._tickUnit = tickUnit;
        }

        initDateFormatter(this._tickUnit);
        Calendar startDate = _timeExtent.getStartDate();
        Calendar endDate = _timeExtent.getEndDate();

        // Set slider limits.
        _rangeSlider.setMinimum(0);
        _rangeSlider.setMaximum(getUpperLimit(this._tickUnit));

        // getUpperLimit may have calculated a tick unit and step size
        _tickSize = _unitBasedStepSizeHint > 0 ? _unitBasedStepSizeHint : tickSize;
        _rangeSlider.setMajorTickSpacing(_tickSize);
        _rangeSlider.setMinorTickSpacing(1);

        // Enable slider: it may have been disabled with an unsuccessful call
        // to setupFromLayer
        _rangeSlider.setEnabled(true);

        if (_dateFormatter != null) {
            _lblLowerValue.setText(_dateFormatter.format(startDate.getTime()));
            _lblUpperValue.setText(_dateFormatter.format(endDate.getTime()));
        } else {
            _lblLowerValue.setText(new Integer(startDate.get(_dateField))
                    .toString());
            _lblUpperValue.setText(new Integer(endDate.get(_dateField))
                    .toString());
        }
    }

    /**
     * Gets the time interval defined by the two thumbs on the slider.
     *
     * @return the time interval; null, if the time extent is not set yet.
     */
    public TimeExtent getTimeInterval() {
        TimeExtent retVal = null;
        if (_timeExtent != null) {
            Calendar startDate = (Calendar) _timeExtent.getStartDate().clone();
            startDate.add(_dateField, _rangeSlider.getValue());
            Calendar endDate = (Calendar) startDate.clone();
            endDate.add(_dateField, _rangeSlider.getExtent());

            retVal = new TimeExtent(startDate, endDate);
        }

        return retVal;
    }

    /**
     * Gets the time of the thumb of the slider. If the {@link TimeMode} is
     * {@link TimeMode#TimeExtent}, then this returns the value of the second thumb
     * of the slider.
     *
     * @return the time of the second thumb; null, if time extent is not set yet.
     */
    public Calendar getTime() {
        TimeExtent interval = getTimeInterval();
        if (interval != null) {
            return interval.getEndDate();
        }
        return null;
    }

    /**
     * Sets the time instant and updates the thumb on the slider.
     * @param time instant to be set.
     * @throws RuntimeException
     * If the time extent is not set yet. <br>
     * If the {@link TimeMode} is {@link TimeMode#TimeExtent}.
     * For TimeMode.TimeExtent use {@link #setTimeInterval(TimeExtent)}.
     * @throws IllegalArgumentException
     * If the specified time is not within the time extent.
     * @see #setTimeInterval(TimeExtent)
     */
    public void setTime(Calendar time) {
        if (_timeExtent == null) {
            throw new RuntimeException("Time extent is not set yet.");
        }
        if (getTimeMode() == TimeMode.TimeExtent) {
            throw new RuntimeException("setTime() cannot be used in time mode " + getTimeMode());
        }

        int n = getTicksToEndDate(time);

        if (n < _rangeSlider.getMinimum() || n > _rangeSlider.getMaximum()) {
            throw new IllegalArgumentException("Intervlas should be in valid range [" +
                _timeExtent.getStartDate() + "-" + _timeExtent.getEndDate() + "].");
        }

        if (getTimeMode() == TimeMode.TimeInstant) {
            _rangeSlider.setValue(_rangeSlider.getMaximum() - n);
        } else if (getTimeMode() == TimeMode.CumulativeFromStart) {
            _rangeSlider.setExtent(_rangeSlider.getMaximum() - (n + _rangeSlider.getValue()));
        }
    }

    /**
     * Sets the time interval and updates the two thumbs of the slider.
     * @param timeInterval time interval to set.
     * @throws RuntimeException
     * If the time extent is not set yet. <br>
     * If the {@link TimeMode} is not {@link TimeMode#TimeExtent}.
     * Use {@link #setTime(Calendar)} instead. <br>
     * @throws IllegalArgumentException
     * If the specified interval values are not within the time extent. <br>
     * If the start of interval is not before end of interval.
     * @see #setTime(Calendar)
     */
    public void setTimeInterval(TimeExtent timeInterval) {
        Calendar intervalStart = timeInterval.getStartDate();
        Calendar intervalEnd   = timeInterval.getEndDate();

        if (_timeExtent == null) {
            throw new RuntimeException("Time extent is not set yet.");
        }
        if (getTimeMode() != TimeMode.TimeExtent) {
            throw new RuntimeException("setTimeInterval() cannot be used in time mode " + getTimeMode());
        }

        int n = getTicksToEndDate(intervalStart);
        int m = getTicksToEndDate(intervalEnd);

        if (n < _rangeSlider.getMinimum() || n > _rangeSlider.getMaximum() ||
            m < _rangeSlider.getMinimum() || m > _rangeSlider.getMaximum()) {
            throw new IllegalArgumentException("Intervlas should be in valid range [" +
                _timeExtent.getStartDate() + "-" + _timeExtent.getEndDate() + "].");
        }
        if (n < m) {
            throw new IllegalArgumentException("Start of interval should be before its end.");
        }

        _rangeSlider.setValue(_rangeSlider.getMaximum() - n);
        _rangeSlider.setExtent(_rangeSlider.getMaximum() - (m + _rangeSlider.getValue()));
    }

    /**
     * Sets the start of the time interval and updates the thumbs accordingly.
     * @param intervalStart start of the time interval.
     * @throws RuntimeException
     * If the time extent is not set yet. <br>
     * If the {@link TimeMode} is not {@link TimeMode#TimeExtent}.
     * Use {@link #setTime(Calendar)} instead.<br>
     * @throws IllegalArgumentException
     * If the specified start of interval is not within the time extent. <br>
     * If the specified start of interval is not before current end of interval.
     * @see #setTimeIntervalEnd(Calendar)
     */
    public void setTimeIntervalStart(Calendar intervalStart) {
        if (_timeExtent == null) {
            throw new RuntimeException("Time extent is not set yet.");
        }
        if (getTimeMode() != TimeMode.TimeExtent) {
            throw new RuntimeException("setTimeIntervalStart() cannot be used in time mode " + getTimeMode());
        }

        int n = getTicksToEndDate(intervalStart);

        if (n < _rangeSlider.getMinimum() || n > _rangeSlider.getMaximum()) {
            throw new IllegalArgumentException("Start interval should be in valid range [" +
                _timeExtent.getStartDate() + "-" + _timeExtent.getEndDate() + "].");
        }
        if (n < (_rangeSlider.getMaximum() - _rangeSlider.getValue() - _rangeSlider.getExtent())) {
            throw new IllegalArgumentException("Start of interval cannot be after its end.");
        }

        int newStartValue = _rangeSlider.getMaximum() - n;
        int changeInExtent = _rangeSlider.getValue() - newStartValue;
        _rangeSlider.setValue(newStartValue);
        _rangeSlider.setExtent(_rangeSlider.getExtent() + changeInExtent);
    }

    /**
     * Sets the end of the time interval and updates the thumbs accordingly.
     * @param intervalEnd end of the time interval.
     * @throws RuntimeException
     * If the time extent is not set yet. <br>
     * If the {@link TimeMode} is not {@link TimeMode#TimeExtent}.
     * Use {@link #setTime(Calendar)} instead.<br>
     * @throws IllegalArgumentException
     * If the specified end of interval is not within the time extent. <br>
     * If the specified end of interval is before start of interval.
     * @see #setTimeIntervalStart(Calendar)
     */
    public void setTimeIntervalEnd(Calendar intervalEnd) {
        if (_timeExtent == null) {
            throw new RuntimeException("Time extent is not set yet.");
        }
        if (getTimeMode() != TimeMode.TimeExtent) {
            throw new RuntimeException("setTimeIntervalEnd() cannot be used in time mode " + getTimeMode());
        }

        int n = getTicksToEndDate(intervalEnd);

        if (n < _rangeSlider.getMinimum() || n > _rangeSlider.getMaximum()) {
            throw new IllegalArgumentException("End interval should be in valid range [" +
                _timeExtent.getStartDate() + "-" + _timeExtent.getEndDate() + "].");
        }
        if (n < _rangeSlider.getValue()) {
            throw new IllegalArgumentException("End of interval cannot be before its start.");
        }

        int m = _rangeSlider.getMaximum() - (n + _rangeSlider.getValue());
        _rangeSlider.setExtent(m);
    }

    /**
     * Get the current playback rate for the play button in milliseconds.
     *
     * @return the playbackRate
     */
    public int getPlaybackRate() {
        return _playbackRate;
    }

    /**
     * Set the current playback rate for the play button in milliseconds.
     *
     * @param playbackRate
     *            the playbackRate to set
     */
    public void setPlaybackRate(int playbackRate) {
        _playbackRate = playbackRate;
    }

    /**
     * Attempt to set up the slider using information from the given layer.
     *
     * @param layer
     *            to setup slider with
     */
    public void setupFromLayer(TimeAwareLayer layer) {
        if (_timeLayers.add(layer)) {
            TimeInfo layerTimeInfo = layer.getTimeInfo();
            TimeOptions exportOptions = layerTimeInfo.getExportOptions();
            if(exportOptions != null){
                if (exportOptions.isTimeDataCumulative()) {
                    setTimeMode(TimeMode.CumulativeFromStart);
                } else {
                    setTimeMode(TimeMode.TimeExtent);
                }
            }else{
                setTimeMode(TimeMode.TimeExtent);
            }
            setTimeExtent(layer.getTimeInfo().getTimeExtent(),
                    layerTimeInfo.getTimeInterval(),
                    layerTimeInfo.getTimeIntervalUnits());
        }
    }

    /**
     * Adds the given layer that will be filtered by this control.
     *
     * @param layer
     *            the layer to filter
     * @return true, if successful
     */
    public boolean addLayer(TimeAwareLayer layer) {
        return _timeLayers.add(layer);
    }

    /**
     * Removes the given layer.
     *
     * @param layer
     *            the layer to remove
     * @return true, if successful
     */
    public boolean removeLayer(TimeAwareLayer layer) {
        return _timeLayers.remove(layer);
    }

    /**
     * Move the two thumbs forward by a single tick.
     */
    public void singleTickForward() {
        if (_rangeSlider.getRangeMode() == RangeMode.CumulativeFromStart) {
            _rangeSlider.setExtent(_rangeSlider.getExtent() + _tickSize);
        } else {
            _rangeSlider.setValue(_rangeSlider.getValue() + _tickSize);
        }

        if (_rangeSlider.getValue() + _rangeSlider.getExtent() >= _rangeSlider
                .getMaximum()) {
            stop();
        }
    }

    /**
     * Move the two thumbs backward by a single tick.
     */
    public void singleTickBackward() {
        if (_rangeSlider.getRangeMode() == RangeMode.CumulativeFromStart) {
            _rangeSlider.setExtent(_rangeSlider.getExtent() - _tickSize);
        } else {
            _rangeSlider.setValue(_rangeSlider.getValue() - _tickSize);
        }

        if (_rangeSlider.getValue() + _rangeSlider.getExtent() <= _rangeSlider
                .getMinimum()) {
            stop();
        }
    }

    /**
     * Animate the two thumbs. The animation will run until the upper thumb
     * reaches the upper limit.
     */
    public void play() {
        if (_playbackTimer == null) {
            _playbackTimer = new Timer(_playbackRate, new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    singleTickForward();
                }
            });
        }

        _playbackTimer.start();
    }

    /**
     * Pause the animation. The <code>play</code> method will resume from the
     * current thumb positions.
     */
    public void stop() {
        if (_playbackTimer != null) {
            _playbackTimer.stop();
            _btnPlay.setSelected(false);
        }
    }

    /**
     * Initialise the date formatter based on the unit of the time interval. For
     * centuries, decades and years, the formatter will just give the year. For
     * months, weeks and days, the whole date will be given. The date format
     * will be the long date format for the current locale. For hours, minutes,
     * seconds and milliseconds, the formatter will give the long time formatted
     * according to the current locale.
     *
     * @param tickUnit
     *            the tick unit
     */
    private void initDateFormatter(Units tickUnit) {
        if (!_userSetDateFormat) {
            switch (tickUnit) {
            case Centuries:
            case Decades:
            case Years:
                _dateFormatter = null;
                break;
            case Months:
            case Weeks:
            case Days:
                _dateFormatter = DateFormat.getDateInstance(DateFormat.LONG);
                break;
            case Hours:
            case Minutes:
                _dateFormatter = DateFormat.getDateTimeInstance(
                        DateFormat.SHORT, DateFormat.LONG);
                break;
            case Seconds:
            case Milliseconds:
                _dateFormatter = DateFormat.getTimeInstance(DateFormat.LONG);
                break;
            default:
              break;
            }
        }
    }

    /**
     * Gets the upper limit value on the slider by counting the number of time
     * units between the lower limit and the upper limit. This is done by
     * repeatedly adding the time unit onto the lower limit and seeing if it is
     * still less than the upper limit. Doing it this way rather than simply
     * converting to milliseconds and subtracting allows us to take account of
     * the varying number days in each month and year.
     *
     * @param tickUnit
     *            the tick unit
     * @return the upper limit
     */
    private int getUpperLimit(Units tickUnit) {
        _dateField = Calendar.YEAR;

        if (tickUnit == Units.Unknown) {
            tickUnit = determineTickUnit();
        }

        switch (tickUnit) {
        case Centuries:
        case Decades:
        case Years:
            break;
        case Months:
            _dateField = Calendar.MONTH;
            break;
        case Weeks:
            _dateField = Calendar.WEEK_OF_YEAR;
            break;
        case Days:
            _dateField = Calendar.DAY_OF_MONTH;
            break;
        case Hours:
            _dateField = Calendar.HOUR_OF_DAY;
            break;
        case Minutes:
            _dateField = Calendar.MINUTE;
            break;
        case Seconds:
            _dateField = Calendar.SECOND;
            break;
        case Milliseconds:
            _dateField = Calendar.MILLISECOND;
            break;
        default:
          break;
        }

        return getTicksToEndDate(_timeExtent.getStartDate());
    }

    /**
     * Gets the number of ticks between the specified fromDate to the the end date of the
     * time extent.
     * @param fromDate number of ticks will be counted from this date.
     * @return the number of ticks.
     */
    private int getTicksToEndDate(Calendar fromDate) {
        int ticks = 0;
        Calendar fromDateCopy = (Calendar) fromDate.clone();
        while (fromDateCopy.before(_timeExtent.getEndDate())) {
            fromDateCopy.add(_dateField, getIncrement(_tickUnit));
            ++ticks;
        }
        return ticks;
    }

    /**
     * Gets the amount of increment to be done based on tick unit.
     * @param tickUnit tick unit.
     * @return the amount of increment to be done based on tick unit.
     */
    private int getIncrement(Units tickUnit) {
        int increment = 1;
        switch (tickUnit) {
            // centuries and decades are used relative to number of years
            case Centuries:
                increment = 100;
                break;
            case Decades:
                increment = 10;
                break;
            default:
                break;
        }
        return increment;
    }

    private Units determineTickUnit() {
        Units retVal = Units.Unknown;
        _unitBasedStepSizeHint = 0;

        Calendar startDate = _timeExtent.getStartDate();
        Calendar endDate = _timeExtent.getEndDate();

        int yearDiff = endDate.get(Calendar.YEAR) - startDate.get(Calendar.YEAR);
        if(yearDiff > 10){
            retVal = Units.Years;
            _unitBasedStepSizeHint = yearDiff / 10;
        }else if (yearDiff > 0){
            retVal = Units.Years;
        }else{
            int monthDiff = endDate.get(Calendar.MONTH) - startDate.get(Calendar.MONTH);
            if(monthDiff > 4){
                // Less than this would give around 16 week steps
                retVal = Units.Months;
            }else if(monthDiff > 1){
                // Less than this would give about 30 day steps
                retVal = Units.Weeks;
            }else if(monthDiff > 0){
                retVal = Units.Days;
            } else {
                if (endDate.get(Calendar.DAY_OF_MONTH)
                        - startDate.get(Calendar.DAY_OF_MONTH) > 0) {
                    retVal = Units.Days;
                } else {
                    int hourDiff = endDate.get(Calendar.HOUR_OF_DAY)
                            - startDate.get(Calendar.HOUR_OF_DAY);

                    if(hourDiff > 4){
                        retVal = Units.Hours;
                    }else if(hourDiff > 0){
                        // Could have up to 120 minutes so we want to
                        // increase the step size
                        _unitBasedStepSizeHint = hourDiff * 4;
                        retVal = Units.Minutes;
                    }else{
                        int minuteDiff = endDate.get(Calendar.MINUTE)
                            - startDate.get(Calendar.MINUTE);
                        if(minuteDiff > 30){
                            _unitBasedStepSizeHint = 4;
                            retVal = Units.Minutes;
                        }else if(minuteDiff > 10){
                            _unitBasedStepSizeHint = 2;
                            retVal = Units.Minutes;
                        }else if(minuteDiff > 0){
                            retVal = Units.Minutes;
                        } else {
                            int secondDiff = endDate.get(Calendar.SECOND)
                                    - startDate.get(Calendar.SECOND);
                            if (secondDiff > 30) {
                                _unitBasedStepSizeHint = 4;
                                retVal = Units.Seconds;
                            } else if (secondDiff > 10) {
                                _unitBasedStepSizeHint = 2;
                                retVal = Units.Seconds;
                            } else if (secondDiff > 0) {
                                retVal = Units.Seconds;
                            }else{
                                int milliSecDiff = endDate.get(Calendar.MILLISECOND)
                                    - startDate.get(Calendar.MILLISECOND);

                                retVal = Units.Milliseconds;
                                _unitBasedStepSizeHint = (int)(milliSecDiff / 10);
                            }
                        }
                    }
                }
            }
        }
        return retVal;
    }

    /**
     * Initialise the gui.
     */
    protected void initGui() {
        ToolTipManager.sharedInstance().setInitialDelay(100);

        setBorder(new EmptyBorder(2, 2, 2, 2));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        _lblTitle = new JLabel("Title");
        _lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(_lblTitle);

        JPanel pnlLabels = new JPanel();
        add(pnlLabels);
        pnlLabels.setLayout(new BoxLayout(pnlLabels, BoxLayout.X_AXIS));

        _lblLowerValue = new JLabel("");
        pnlLabels.add(_lblLowerValue);

        Component horizontalGlue = Box.createHorizontalGlue();
        pnlLabels.add(horizontalGlue);

        _lblCurrentRange = new JLabel("");
        pnlLabels.add(_lblCurrentRange);

        Component horizontalGlue_1 = Box.createHorizontalGlue();
        pnlLabels.add(horizontalGlue_1);

        _lblUpperValue = new JLabel("");
        _lblUpperValue.setAlignmentX(Component.RIGHT_ALIGNMENT);
        pnlLabels.add(_lblUpperValue);

        JPanel pnlSlider = new JPanel();
        add(pnlSlider);
        pnlSlider.setLayout(new BoxLayout(pnlSlider, BoxLayout.X_AXIS));

        _btnPlay = new JToggleButton("");
        _btnPlay.setAction(playAction);
        _btnPlay.setMargin(new Insets(1, 1, 1, 1));
        _btnPlay.setSelectedIcon(new ImageIcon(
                JTimeSlider.class
                        .getResource("/com/esri/client/toolkit/images/GenericBluePause16.png")));
        _btnPlay.setIcon(new ImageIcon(
                JTimeSlider.class
                        .getResource("/com/esri/client/toolkit/images/GenericBlueRightArrowNoTail16.png")));
        pnlSlider.add(_btnPlay);

        _rangeSlider = new RangeSlider();
        _rangeSlider.setUI(new BasicRangeSliderUI(_rangeSlider));
        _rangeSlider.setPaintTicks(true);
        _rangeSlider.setRangeMode(timeModeToRangeMode(_timeMode));
        _rangeSlider.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                handleSliderValueChange(e);
            }
        });
        pnlSlider.add(_rangeSlider);

        _btnStepBack = new JButton("");
        _btnStepBack.setAction(stepBackAction);
        _btnStepBack.setMargin(new Insets(1, 1, 1, 1));
        _btnStepBack
                .setIcon(new ImageIcon(
                        JTimeSlider.class
                                .getResource("/com/esri/client/toolkit/images/GenericBlueLeftArrowNoTail16.png")));
        pnlSlider.add(_btnStepBack);

        _btnStepForward = new JButton("");
        _btnStepForward.setAction(stepForwardAction);
        _btnStepForward.setMargin(new Insets(1, 1, 1, 1));
        _btnStepForward
                .setIcon(new ImageIcon(
                        JTimeSlider.class
                                .getResource("/com/esri/client/toolkit/images/GenericBlueRightArrowNoTail16.png")));
        pnlSlider.add(_btnStepForward);
    }

    /**
     * Handle slider value changes.
     *
     * @param e
     *            the change event
     */
    protected void handleSliderValueChange(ChangeEvent e) {
        if (!_rangeSlider.getValueIsAdjusting()) {
            TimeExtent curExtent = getTimeInterval();
            displayCurrentRange(curExtent);
            updateLayers(curExtent);
        }
    }

    private RangeMode timeModeToRangeMode(TimeMode timeMode) {
        RangeMode retVal = RangeMode.CumulativeFromStart;

        switch (timeMode) {
        case CumulativeFromStart:
            retVal = RangeMode.CumulativeFromStart;
            break;
        case TimeExtent:
            retVal = RangeMode.Range;
            break;
        case TimeInstant:
            retVal = RangeMode.SingleValue;
            break;
        }

        return retVal;
    }

    /**
     * Formats the current range label.
     *
     * @param extent
     *            the current time extent
     */
    private void displayCurrentRange(TimeExtent extent) {
        if (_lblCurrentRange.isVisible()) {
            if (_dateFormatter != null) {
                _lblCurrentRange.setText(String.format("%s -> %s",
                        _dateFormatter.format(extent.getStartDate().getTime()),
                        _dateFormatter.format(extent.getEndDate().getTime())));
            } else if (extent != null) {
                _lblCurrentRange.setText(String.format("%d -> %d", extent
                        .getStartDate().get(_dateField), extent.getEndDate()
                        .get(_dateField)));
            }
        }
    }

    /**
     * Update the layers with the current time interval.
     *
     * @param extent
     *            the current time interval
     */
    private void updateLayers(TimeExtent extent) {
        for (TimeAwareLayer curLayer : _timeLayers) {
            curLayer.setCurrentTimeExtent(extent);
        }
    }

    /**
     * Handle clicks on the play button.
     */
    private class PlayAction extends AbstractAction {
        private static final long serialVersionUID = 1L;

        /**
         * Instantiates a new play action.
         */
        public PlayAction() {
            putValue(SHORT_DESCRIPTION, "Play or pause playback");
        }

        /*
         * (non-Javadoc)
         *
         * @see
         * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent
         * )
         */
        public void actionPerformed(ActionEvent e) {
            if (_btnPlay.isSelected()) {
                play();
            } else {
                stop();
            }
        }
    }

    /**
     * Handle clicks on the step back button.
     */
    private class StepBackAction extends AbstractAction {
        private static final long serialVersionUID = 1L;

        /**
         * Instantiates a new step back action.
         */
        public StepBackAction() {
            putValue(SHORT_DESCRIPTION, "Move the slider back one tick");
        }

        /*
         * (non-Javadoc)
         *
         * @see
         * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent
         * )
         */
        public void actionPerformed(ActionEvent e) {
            singleTickBackward();
        }
    }

    /**
     * Handle clicks on the step forward button.
     */
    private class StepForwardAction extends AbstractAction {
        private static final long serialVersionUID = 1L;

        /**
         * Instantiates a new step forward action.
         */
        public StepForwardAction() {
            putValue(SHORT_DESCRIPTION, "Move the slider forward one tick");
        }

        /*
         * (non-Javadoc)
         *
         * @see
         * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent
         * )
         */
        public void actionPerformed(ActionEvent e) {
            singleTickForward();
        }
    }
}
