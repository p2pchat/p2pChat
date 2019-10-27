package Annotations;

/**
 * It is either severe, medium severe, low or not severe.
 * Scale:
 * Severe = drop what you are doing and get right on that.
 * medium Severe = Get done with what you are doing. After that
 *                  then get right on the problem.
 * Low = It could wait a few projects. In no worry, but must get done before
 *          the sprint.
 * NOT severe at all = No rush, can be done after the sprint.
 */
public enum Priority
{
    SEVERE, MEDIUM_SEVERE, LOW, NOT_SEVERE_AT_ALL
}
