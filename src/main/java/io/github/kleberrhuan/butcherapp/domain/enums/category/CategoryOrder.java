package io.github.kleberrhuan.butcherapp.domain.enums.category;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CategoryOrder {
    FIRST(1),
    SECOND(2),
    THIRD(3);

    private final int order;
}
