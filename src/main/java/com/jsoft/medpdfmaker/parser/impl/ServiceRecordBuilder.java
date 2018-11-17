import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

import com.jsoft.medpdfmaker.domain.ServiceRecord;
import com.jsoft.medpdfmaker.parser.ObjectBuilder;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

import edu.emory.mathcs.backport.java.util.Arrays;

public class ServiceRecordBuilder implements ObjectBuilder<ServiceRecord> {

    private ServiceRecord resultRecord = new ServiceRecord();
    
    @Override
    public void setAttributeValue(String attrName, Cell value) {

    }

    private void setRefId(Cell cell) {
        resultRecord.setRefId(extractStringValue(cell));
    }

    private void setCancelled(Cell cell) {
        resultRecord.setCancelled(extractBooleanValue(cell));
    }

    private void setLName(Cell cell) {
        resultRecord.setLName(extractStringValue(cell));
    }

    private void setFName(Cell cell) {
        resultRecord.setFName(extractStringValue(cell));
    }

    private void setMemberId(Cell cell) {
        resultRecord.setMemberId(extractStringValue(cell));
    }

    private void setDob(Cell cell) {
        resultRecord.setDob(extractDateValue(cell));
    }

	private void setPickupDate(Cell pickupcellDateCell) {
        resultRecord.setDob(extractDateValue(cell));
    }

    private void setPickupTime(Cell cell) {
        resultRecord.setPickupTime(extractTimeValue(cell));
    }

    private void setApptTime(Cell cell) {
        resultRecord.setApptTime(extractTimeValue(cell));
    }

    private void setOrigin(Cell cell) {
        resultRecord.setOrigin(extractStringValue(cell));
    }

    private void setDestination(Cell cell) {
        resultRecord.setDestination(extractStringValue(cell));
    }

    private void setWheelChairYesNo(Cell cell) {
        resultRecord.setWheelChairYesNo(extractBooleanValue(cell));
    }

    private void setTotalPassengers(Cell cell) {
        resultRecord.setTotalPassengers(extractIntegerValue(cell));
    }

    private void setNotes(Cell cell) {
        resultRecord.setNotes(extractStringValue(cell));
    }

    private void setTelephone(Cell cell) {
        resultRecord.setTelephone(extractStringValue(cell));
    }

    private void setCoordinatorInitials(Cell cell) {
        resultRecord.setCoordinatorInitials(extractStringValue(cell));
    }

    private void setCity(Cell cell) {
        resultRecord.setCity(extractStringValue(cell));
    }

    private void setState(Cell cell) {
        resultRecord.setState(extractStringValue(cell));
    }

    private void setZipCode(Cell cell) {
        resultRecord.setZipCode(extractStringValue(cell));
    }

    private void setAreaCode(Cell cell) {
        resultRecord.setAreaCode(extractStringValue(cell));
    }

    private void setPhone(Cell cell) {
        resultRecord.setPhone(extractStringValue(cell));
    }

    private LocalTime extractTimeValue(Cell cell) {
        return null;
    }

    private LocalDate extractDateValue(Cell cell) {
        return null;
    }

    private Boolean extractBooleanValue(Cell cell) {
        Boolean result = false;
        if (CellType.BOOLEAN.equals(cell.getCellType())) {
            result = cell.getBooleanCellValue();
        } else if (CellType.NUMERIC.equals(cell.getCellType())) {
            double numVal = cell.getNumericCellValue();
            result = Math.abs(0 - numVal) > 0.01;
        } else if (CellType.STRING.equals(cell.getCellType())) {
            String strVal = StringUtils.upperCase(StringUtils.trim(cell.getStringCellValue()));
            Set<String> possibleTrues = new HashSet<>();
            possibleTrues.add("Y");
            possibleTrues.add("YES");
            possibleTrues.add("TRUE");
            possibleTrues.add("X");
            result = possibleTrues.contains(strVal);
        } 
        return result;
    }     

    private Integer extractIntegerValue(Cell cell) {
        Integer result;
        if (CellType.NUMERIC.equals(cell.getCellType())) {
            double numVal = cell.getNumericCellValue();    
            result = (int)Math.round(numVal);
        } else if (CellType.BOOLEAN.equals(cell.getCellType())) {
            boolean boolVal = cell.getBooleanCellValue();
            result = boolVal ? 0 : 1;
        } else if (CellType.BLANK.equals(cell.getCellType())) {
            result = null;
        } else if (CellType.STRING.equals(cell.getCellType())) {
            String strVal = StringUtils.trim(cell.getStringCellValue());
            try {
                result = StringUtils.isEmpty(strVal) ? null : Integer.valueOf(strVal);
            } catch (NumberFormatException e) {
                result = null;
            }
        }    
        return result;
    }

    private String extractStringValue(Cell cell) {
        return cell == null ? null : cell.getStringCellValue();
    }

    @Override
    public ServiceRecord build() {
        final ServiceRecord result = resultRecord;
        resultRecord = new ServiceRecord();
        return result;
	}

}