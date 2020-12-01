package org.xiyoulinux.qqbot.framework.handle.mirai.message.enhance;

import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.util.List;

/**
 * @author xuanc
 * @version 1.0
 * @date 2020/12/1 下午2:05
 */
@Data
@Accessors(chain = true)
public class ListFilter<T> implements Filter<List<T>> {

    private boolean valid;

    private T[] allowList;

    private T[] blockList;

    public ListFilter(T[] allowList, T[] blockList) {
        this.valid = true;
        this.allowList = allowList;
        this.blockList = blockList;
    }

    public static <E> ListFilter<E> of(E[] allowList, E[] blockList) {
        return new ListFilter<>(allowList, blockList);
    }

    @Override
    public boolean getValid() {
        return valid;
    }

    @Override
    public boolean filterFunc(List<T> maybeVales) {
        if (CollectionUtils.isEmpty(maybeVales)) {
            return false;
        }
        if (ArrayUtils.isEmpty(allowList) && ArrayUtils.isEmpty(blockList)) {
            return false;
        }
        boolean flag = !ArrayUtils.isNotEmpty(allowList);
        final T[] conditionArr = ArrayUtils.isNotEmpty(allowList) ? allowList : blockList;
        // simply !(flag ^ ArrayUtils.contains(...))
        return flag == maybeVales.stream().anyMatch(val -> ArrayUtils.contains(conditionArr, val));
    }

}
