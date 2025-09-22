package com.everyonewaiter.domain.notification;

import static java.util.Objects.requireNonNull;

import com.everyonewaiter.domain.shared.PhoneNumber;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class AlimTalkMessage {

  private final String templateCode;

  private final String to;

  private final String content;

  private final boolean useSmsFailover;

  private final List<AlimTalkButton> buttons = new ArrayList<>();

  public AlimTalkMessage(AlimTalkTemplate template, PhoneNumber to, Object... variables) {
    this.templateCode = requireNonNull(template).getTemplateCode();
    this.to = requireNonNull(to).value();
    this.content = requireNonNull(template).createContent(variables);
    this.useSmsFailover = true;
  }

  public void addButton(AlimTalkWeblinkButtonTemplate buttonTemplate, Object... arguments) {
    buttons.add(buttonTemplate.createButton(arguments));
  }

  public List<AlimTalkButton> getButtons() {
    return Collections.unmodifiableList(buttons);
  }

}
